package py.pol.una.ii.pw.dao;

import javax.ejb.EJB;
import javax.ejb.Remove;
import javax.ejb.Stateful;

import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.managed.ManagedTransactionFactory;

import py.pol.una.ii.pw.mapper.ClienteMapper;
import py.pol.una.ii.pw.mapper.CompraMapper;
import py.pol.una.ii.pw.mapper.ProductoMapper;
import py.pol.una.ii.pw.mapper.ProveedorMapper;
import py.pol.una.ii.pw.mapper.VentaMapper;
import py.pol.una.ii.pw.model.Producto;
import py.pol.una.ii.pw.model.Venta;
import py.pol.una.ii.pw.model.VentaDetalle;
import py.pol.una.ii.pw.service.MyBatisSingleton;

@Stateful
public class VentaDAO {
	
	@EJB
	private MyBatisSingleton mb;
	
	private SqlSession session;
	
	private VentaMapper cm;
	
	private ProductoMapper pm;
	
	public void init(){
		TransactionFactory transactionFactory = new ManagedTransactionFactory();
		Environment environment = new Environment("development", transactionFactory, mb.getSource());
		Configuration configuration = new Configuration(environment);
		configuration.addMapper(ClienteMapper.class);
		configuration.addMapper(ProveedorMapper.class);
		configuration.addMapper(CompraMapper.class);
		configuration.addMapper(ProductoMapper.class);
		configuration.addMapper(VentaMapper.class);
		SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);
		session = sqlSessionFactory.openSession();
		try {
			cm = session.getMapper(VentaMapper.class);
			pm = session.getMapper(ProductoMapper.class);
		} catch (Exception e) {
			System.out.println("Error al abrir la base de datos");
			System.out.println(e.getMessage());
		}
	}
	
	public void persist(Venta c){
		if(session!=null){
			try {
				Long id_venta = cm.generarIdVenta();
				cm.persistVenta(id_venta, c.getFecha(), c.getMontoTotal(), c.getCliente().getRuc());
				for(VentaDetalle det: c.getVentaDetalles()){
					cm.nuevoDetalle(cm.generarIdVentaDetalle(), det.getCantidad(), det.getPrecio(), id_venta, det.getProducto().getId());
				}
			} catch (Exception e) {
				rollback();
				System.out.println("Hubo un error persist venta 1");
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
				rollback();
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
	
	/*
	public void update(Venta c){
		if(cm!=null){
			try {
				//cm.updateVenta(c.getRuc(), c.getDeuda(),c.getActivo(), c.getNombre());;
			} catch (Exception e) {
				System.out.println("Hubo un error");
				System.out.println(e.getMessage());
			}
			
		}
	}
	
	public Venta findById(String ruc){
		if(cm!=null){
			return cm.getVenta(ruc);
		}else{
			return null;
		}
	}
	
	public List<Venta> findAll(){
		if(cm!=null){
			return cm.getAllVentas();
		}else{
			return null;
		}
	}*/
	
	@Remove
	public void commit(){
		if(session!=null){
			session.commit();
			session.close();
		}
	}
	
	@Remove
	public void rollback(){
		if(session!=null){
			session.rollback();
			this.close();
		}
	}
	
	@Remove
	public void close(){
		if(session!=null){
			try {
				session.close();
				session = null;
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
	}
}
