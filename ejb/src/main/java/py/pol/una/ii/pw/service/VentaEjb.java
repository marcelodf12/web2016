package py.pol.una.ii.pw.service;

import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.managed.ManagedTransactionFactory;

import py.pol.una.ii.pw.dto.VentaDetalleDto;
import py.pol.una.ii.pw.dto.VentaDto;
import py.pol.una.ii.pw.mapper.VentaMapper;
import py.pol.una.ii.pw.mapper.ProductoMapper;
import py.pol.una.ii.pw.mapper.ClienteMapper;
import py.pol.una.ii.pw.model.Venta;
import py.pol.una.ii.pw.model.Producto;
import py.pol.una.ii.pw.model.Cliente;
import py.pol.una.ii.pw.util.Respuesta;

@Stateless
public class VentaEjb {
	
	@EJB
	private MyBatisSingleton mb;
	
	private SqlSession session;
	
	private VentaMapper cm;
	
	private ProductoMapper pm;

	private ClienteMapper cmCliente;

	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Respuesta<Venta> alta(VentaDto ventaDto){
		Respuesta<Venta> r = new Respuesta<Venta>();
		try {
			init();
			Venta c = this.nuevaVenta(ventaDto);
			if(c == null){
				r.setMessages("La venta no se puede persistir");
				r.setReason("No existe un producto del detalle");
				r.setSuccess(false);
				r.setData(null);
				return r;
			}
				
			
			if(c.getCliente()==null){
				r.setMessages("La venta no se puede persistir");
				r.setReason("No existe el cliente");
				r.setSuccess(false);
				r.setData(null);
			}else{
				r.setSuccess(true);
				r.setMessages("Venta guardada correctamente");
				r.setData(c);
			}
		} catch (Exception e) {
			r.setData(null);
			r.setMessages("Error al persistir la venta");
			r.setReason(e.getMessage());
			e.printStackTrace();
		} finally {
			session.close();
		}
		return r;
	}
	
	
	public Respuesta<List<Venta>> listarTodos(){
		Respuesta<List<Venta>> r = new Respuesta<List<Venta>>();
		try {
			init();
			List<Venta> data = this.findAll();
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
		} finally {
			session.close();
		}
		return r;
	}
	
	private List<Venta> findAll(){
/*		TypedQuery<Venta> query = em.createQuery(
				"SELECT c FROM Venta c", Venta.class);
		List<Venta> e = query.getResultList();
		if(e.size() > 0) {
			return e;			
		}*/
		return null;
	}
	
	private Cliente findByIdCliente(String ruc){
		if(cmCliente!=null){
			return cmCliente.getCliente(ruc);
		}else{
			return null;
		}
	}
	
	private Venta nuevaVenta(VentaDto ventaDto){
		Venta venta = new Venta(ventaDto);
		Cliente cliente = findByIdCliente(ventaDto.getRucCliente());
		venta.setCliente(cliente);
		Long id_venta = cm.generarIdVenta();
		Integer montoTotal = 0;
		for(VentaDetalleDto det : ventaDto.getVentaDetalles()){
			montoTotal += det.getCantidad() * det.getPrecio();
		}
		cm.persistVenta(id_venta, new Date(), montoTotal, cliente.getRuc());
		
		for(VentaDetalleDto det : ventaDto.getVentaDetalles()){
			Producto p = pm.getProducto(det.getIdProducto());
			if(p!=null){
				cm.nuevoDetalle(cm.generarIdVentaDetalle(), det.getCantidad(), det.getPrecio(), id_venta, p.getId());
				pm.udateProductoStock(p.getId(), p.getStock()-det.getCantidad());
			}else{
				session.rollback();
				break;
			}
		}
		return venta;
	}
	
	private void init(){
		TransactionFactory transactionFactory = new ManagedTransactionFactory();
		Environment environment = new Environment("development", transactionFactory, mb.getSource());
		Configuration configuration = new Configuration(environment);
		configuration.addMapper(ClienteMapper.class);
		configuration.addMapper(VentaMapper.class);
		configuration.addMapper(ProductoMapper.class);
		SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);
		session = sqlSessionFactory.openSession();
		try {
			cm = session.getMapper(VentaMapper.class);
			pm = session.getMapper(ProductoMapper.class);
			cmCliente = session.getMapper(ClienteMapper.class);
		} catch (Exception e) {
			System.out.println("Error al abrir la base de datos");
			System.out.println(e.getMessage());
		}
	}
}
