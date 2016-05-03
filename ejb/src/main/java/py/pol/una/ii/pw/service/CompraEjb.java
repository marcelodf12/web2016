package py.pol.una.ii.pw.service;

import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

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
import py.pol.una.ii.pw.model.CompraDetalle;
import py.pol.una.ii.pw.model.Producto;
import py.pol.una.ii.pw.model.Proveedor;
import py.pol.una.ii.pw.util.Respuesta;

@Stateless
public class CompraEjb {
	
	@PersistenceContext
	private EntityManager em;
	
	@EJB
	private MyBatisSingleton mb;
	
	private SqlSession session;
	
	private CompraMapper cm;
	
	private ProductoMapper pm;

	private ProveedorMapper cmProveedor;

	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Respuesta<Compra> alta(CompraDto compraDto){
		Respuesta<Compra> r = new Respuesta<Compra>();
		try {
			Compra c = this.nuevaCompra(compraDto);
			if(c == null){
				r.setMessages("La compra no se puede persistir");
				r.setReason("No existe un producto del detalle");
				r.setSuccess(false);
				r.setData(null);
				return r;
			}
				
			
			if(c.getProveedor()==null){
				r.setMessages("La compra no se puede persistir");
				r.setReason("No existe el proveedor");
				r.setSuccess(false);
				r.setData(null);
			}else{
				r.setSuccess(true);
				r.setMessages("Compra guardada correctamente");
				r.setData(c);
			}
		} catch (Exception e) {
			r.setData(null);
			r.setMessages("Error al persistir la compra");
			r.setReason(e.getMessage());
			e.printStackTrace();
		}
		return r;
	}
	
	
	public Respuesta<List<Compra>> listarTodos(){
		Respuesta<List<Compra>> r = new Respuesta<List<Compra>>();
		try {
			List<Compra> data = this.findAll();
			if(data == null){
				r.setMessages("La base de datos esta vacia");
				r.setSuccess(false);
			}else{
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
	
	private List<Compra> findAll(){
		TypedQuery<Compra> query = em.createQuery(
				"SELECT c FROM Compra c", Compra.class);
		List<Compra> e = query.getResultList();
		if(e.size() > 0) {
			return e;			
		}
		return null;
	}
	
	public Proveedor findByIdProveedor(String ruc){
		if(cmProveedor!=null){
			return cmProveedor.getProveedor(ruc);
		}else{
			return null;
		}
	}
	
	private Compra nuevaCompra(CompraDto compraDto){
		init();
		Compra compra = new Compra(compraDto);
		Proveedor proveedor = findByIdProveedor(compraDto.getRucProveedor());
		compra.setProveedor(proveedor);
		Integer montoTotal = 0;
		compra.setFecha(new Date());
		for(CompraDetalleDto det : compraDto.getCompraDetalles()){
			montoTotal += det.getPrecio() * det.getCantidad();
			CompraDetalle nuevoDet = new CompraDetalle();
			nuevoDet.setCantidad(det.getCantidad());
			nuevoDet.setPrecio(det.getPrecio());	
			Producto p = findProducto(det.getIdProducto());
			if(p==null){
				return null;
			}else{
				p.setStock(p.getStock()+det.getCantidad());
				nuevoDet.setProducto(p);
				updateProducto(p);
			}
			compra.getCompraDetalles().add(nuevoDet);
			nuevoDet.setCompra(compra);
		}
		compra.setMontoTotal(montoTotal);
		persist(compra);
		return compra;
	}
	
	public void init(){
		TransactionFactory transactionFactory = new ManagedTransactionFactory();
		Environment environment = new Environment("development", transactionFactory, mb.getSource());
		Configuration configuration = new Configuration(environment);
		configuration.addMapper(ProveedorMapper.class);
		configuration.addMapper(ProveedorMapper.class);
		configuration.addMapper(CompraMapper.class);
		configuration.addMapper(ProductoMapper.class);
		configuration.addMapper(CompraMapper.class);
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
	
	public void persist(Compra c){
		if(session!=null){
			try {
				Long id_compra = cm.generarIdCompra();
				cm.persistCompra(id_compra, c.getFecha(), c.getMontoTotal(), c.getProveedor().getRuc());
				for(CompraDetalle det: c.getCompraDetalles()){
					cm.nuevoDetalle(cm.generarIdCompraDetalle(), det.getCantidad(), det.getPrecio(), id_compra, det.getProducto().getId());
				}
			} catch (Exception e) {
				System.out.println("Hubo un error persist compra 1");
				e.printStackTrace();
				System.out.println(e.getMessage());
			}
			
		}
	}
	
	public void updateProducto(Producto p){
		if(session!=null){
			try {
				pm.udateProducto(p.getId(), p.getActivo(), p.getNombre(), p.getPrecio(), p.getStock());
			} catch (Exception e) {
				System.out.println("Error al actualizar stock de producto");
				e.printStackTrace();
				System.out.println(e.getMessage());
			}
		}
	}
	
	public Producto findProducto(Long id){
		if(session!=null){
			return pm.getProducto(id);
		}else{
			return null;
		}
	}

}
