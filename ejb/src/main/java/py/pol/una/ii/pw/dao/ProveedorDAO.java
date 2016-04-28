package py.pol.una.ii.pw.dao;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Remove;
import javax.ejb.Stateful;

import org.apache.ibatis.session.SqlSession;

import py.pol.una.ii.pw.mapper.ProveedorMapper;
import py.pol.una.ii.pw.model.Proveedor;
import py.pol.una.ii.pw.service.MyBatisSingleton;

@Stateful
public class ProveedorDAO {
	
	@EJB
	private MyBatisSingleton mb;
	
	private SqlSession session;
	
	private ProveedorMapper pm;
	
	public void init(){
		session = mb.getSession();
		try {
			pm = session.getMapper(ProveedorMapper.class);
		} catch (Exception e) {
			System.out.println("Error al abrir la base de datos");
			System.out.println(e.getMessage());
		}
	}
	
	public void persist(Proveedor c){
		if(pm!=null){
			try {
				pm.persistProveedor(c.getRuc(), c.getNombre());
			} catch (Exception e) {
				System.out.println("Hubo un error");
				System.out.println(e.getMessage());
			}
			
		}
	}
	public void update(Proveedor c){
		if(pm!=null){
			try {
				pm.updateProveedor(c.getRuc(),c.getActivo(), c.getNombre());;
			} catch (Exception e) {
				System.out.println("Hubo un error");
				System.out.println(e.getMessage());
			}
			
		}
	}
	
	public Proveedor findById(String ruc){
		if(pm!=null){
			return pm.getProveedor(ruc);
		}else{
			return null;
		}
	}
	
	public List<Proveedor> findAll(){
		if(pm!=null){
			return pm.getAllProveedores();
		}else{
			return null;
		}
	}
	
	@Remove
	public void commit(){
		session.commit();
		session.close();
	}
	
	@Remove
	public void rollback(){
		session.rollback();
		this.close();
	}
	
	@Remove
	public void close(){
		try {
			session.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
	}
	
	
	

}
