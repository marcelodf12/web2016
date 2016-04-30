package py.pol.una.ii.pw.dao;

import javax.ejb.EJB;
import javax.ejb.Remove;
import javax.ejb.Stateful;

import org.apache.ibatis.session.SqlSession;

import py.pol.una.ii.pw.mapper.CompraMapper;
import py.pol.una.ii.pw.mapper.ProductoMapper;
import py.pol.una.ii.pw.model.Compra;
import py.pol.una.ii.pw.model.CompraDetalle;
import py.pol.una.ii.pw.model.Producto;
import py.pol.una.ii.pw.service.MyBatisSingleton;

@Stateful
public class CompraDAO {
	
	@EJB
	private MyBatisSingleton mb;
	
	private SqlSession session;
	
	private CompraMapper cm;
	
	private ProductoMapper pm;
	
	public void init(){
		session = mb.getSession();
		try {
			cm = session.getMapper(CompraMapper.class);
			pm = session.getMapper(ProductoMapper.class);
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
				rollback();
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
	public void update(Compra c){
		if(cm!=null){
			try {
				//cm.updateCompra(c.getRuc(), c.getDeuda(),c.getActivo(), c.getNombre());;
			} catch (Exception e) {
				System.out.println("Hubo un error");
				System.out.println(e.getMessage());
			}
			
		}
	}
	
	public Compra findById(String ruc){
		if(cm!=null){
			return cm.getCompra(ruc);
		}else{
			return null;
		}
	}
	
	public List<Compra> findAll(){
		if(cm!=null){
			return cm.getAllCompras();
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
