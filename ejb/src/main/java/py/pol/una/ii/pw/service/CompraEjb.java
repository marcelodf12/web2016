package py.pol.una.ii.pw.service;

import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.managed.ManagedTransactionFactory;

import py.pol.una.ii.pw.dto.CompraDetalleDto;
import py.pol.una.ii.pw.dto.CompraDto;
import py.pol.una.ii.pw.mapper.CompraMapper;
import py.pol.una.ii.pw.mapper.ProductoMapper;
import py.pol.una.ii.pw.mapper.ProveedorMapper;
import py.pol.una.ii.pw.model.Compra;
import py.pol.una.ii.pw.model.Producto;
import py.pol.una.ii.pw.model.Proveedor;
import py.pol.una.ii.pw.util.NoExisteElProductoException;
import py.pol.una.ii.pw.util.Respuesta;

@Stateless
public class CompraEjb {

	@EJB
	private MyBatisSingleton mb;

	private SqlSession session;

	private CompraMapper cm;

	private ProductoMapper pm;

	private ProveedorMapper cmProveedor;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Respuesta<Compra> alta(CompraDto compraDto) {
		Respuesta<Compra> r = new Respuesta<Compra>();
		try {
			Compra c = this.nuevaCompra(compraDto);
			r.setSuccess(true);
			r.setMessages("Compra guardada correctamente");
			r.setData(c);
		}catch (NoExisteElProductoException e){
			r.setData(null);
			r.setMessages("La compra no se puede persistir");
			r.setReason(e.getMessage());
		}catch (NullPointerException e){
			r.setData(null);
			r.setMessages("La compra no se puede persistir");
			r.setReason("No existe el proveedor");
		}catch (PersistenceException e){
			r.setData(null);
			r.setMessages("La compra no se puede persistir");
			r.setReason(e.getClass().toString());
			//System.out.println(e.getClass().toString());
		} catch (Exception e) {
			r.setData(null);
			r.setMessages("La compra no se puede persistir");
			r.setReason(e.getClass().toString());
			//e.printStackTrace();
		} finally {
			session.close();
		}
		return r;
	}

	public Respuesta<List<Compra>> listarTodos() {
		Respuesta<List<Compra>> r = new Respuesta<List<Compra>>();
		try {
			List<Compra> data = this.findAll();
			if (data == null) {
				r.setMessages("La base de datos esta vacia");
				r.setSuccess(false);
			} else {
				r.setMessages("");
				r.setSuccess(true);
			}
			r.setData(data);
		} catch (Exception e) {
			r.setData(null);
			r.setMessages("Error en la base de datos");
			r.setReason(e.getMessage());
			r.setSuccess(false);
		}
		return r;
	}

	private List<Compra> findAll() {
		/*
		 * TypedQuery<Compra> query = em.createQuery( "SELECT c FROM Compra c",
		 * Compra.class); List<Compra> e = query.getResultList(); if(e.size() >
		 * 0) { return e; }
		 */
		return null;
	}

	private Proveedor findByIdProveedor(String ruc) {
		if (cmProveedor != null) {
			return cmProveedor.getProveedor(ruc);
		} else {
			return null;
		}
	}

	private Compra nuevaCompra(CompraDto compraDto) throws NoExisteElProductoException {
		init();
		Compra compra = new Compra(compraDto);
		Proveedor proveedor = findByIdProveedor(compraDto.getRucProveedor());
		compra.setProveedor(proveedor);
		Long id_compra = cm.generarIdCompra();
		Integer montoTotal = 0;
		for (CompraDetalleDto det : compraDto.getCompraDetalles()) {
			montoTotal += det.getCantidad() * det.getPrecio();
		}
		cm.persistCompra(id_compra, new Date(), montoTotal, proveedor.getRuc());

		for (CompraDetalleDto det : compraDto.getCompraDetalles()) {
			Producto p = pm.getProducto(det.getIdProducto());
			if (p != null) {
				cm.nuevoDetalle(cm.generarIdCompraDetalle(), det.getCantidad(), det.getPrecio(), id_compra, p.getId());
				pm.udateProductoStock(p.getId(), p.getStock() + det.getCantidad());
			} else {
				session.rollback();
				throw new NoExisteElProductoException("No existe el producto con id " + det.getIdProducto());
			}
		}
		return compra;
	}

	private void init() {
		TransactionFactory transactionFactory = new ManagedTransactionFactory();
		Environment environment = new Environment("development", transactionFactory, mb.getSource());
		Configuration configuration = new Configuration(environment);
		configuration.addMapper(ProveedorMapper.class);
		configuration.addMapper(CompraMapper.class);
		configuration.addMapper(ProductoMapper.class);
		SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);
		session = sqlSessionFactory.openSession();
		try {
			cm = session.getMapper(CompraMapper.class);
			pm = session.getMapper(ProductoMapper.class);
			cmProveedor = session.getMapper(ProveedorMapper.class);
		} catch (Exception e) {
			System.out.println("Error al abrir la base de datos");
			System.out.println(e.getMessage());
		}
	}

}
