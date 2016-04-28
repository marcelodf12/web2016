package py.pol.una.ii.pw.dao;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Remove;
import javax.ejb.Stateful;

import org.apache.ibatis.session.SqlSession;

import py.pol.una.ii.pw.mapper.ClienteMapper;
import py.pol.una.ii.pw.model.Cliente;
import py.pol.una.ii.pw.service.MyBatisSingleton;

@Stateful
public class ClienteDAO {
	
	@EJB
	private MyBatisSingleton mb;
	
	private SqlSession session;
	
	private ClienteMapper cm;
	
	public void init(){
		session = mb.getSession();
		try {
			cm = session.getMapper(ClienteMapper.class);
		} catch (Exception e) {
			System.out.println("Error al abrir la base de datos");
			System.out.println(e.getMessage());
		}
	}
	
	public void persist(Cliente c){
		if(cm!=null){
			try {
				cm.persistCliente(c.getRuc(), c.getDeuda(), c.getNombre());
			} catch (Exception e) {
				System.out.println("Hubo un error");
				System.out.println(e.getMessage());
			}
			
		}
	}
	public void update(Cliente c){
		if(cm!=null){
			try {
				cm.updateCliente(c.getRuc(), c.getDeuda(),c.getActivo(), c.getNombre());;
			} catch (Exception e) {
				System.out.println("Hubo un error");
				System.out.println(e.getMessage());
			}
			
		}
	}
	
	public Cliente findById(String ruc){
		if(cm!=null){
			return cm.getCliente(ruc);
		}else{
			return null;
		}
	}
	
	public List<Cliente> findAll(){
		if(cm!=null){
			return cm.getAllClientes();
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
