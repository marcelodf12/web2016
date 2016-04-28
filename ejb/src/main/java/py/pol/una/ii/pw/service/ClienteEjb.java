package py.pol.una.ii.pw.service;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import py.pol.una.ii.pw.dao.ClienteDAO;
import py.pol.una.ii.pw.model.Cliente;
import py.pol.una.ii.pw.util.Respuesta;

@Stateless
public class ClienteEjb {
	
	//@PersistenceContext
	//private EntityManager em;
	
	@EJB
	private ClienteDAO clienteDao;
	
	public Respuesta<Cliente> nuevo(Cliente c){
		Respuesta<Cliente> r = new Respuesta<Cliente>();
		try {
			c.setActivo(true);
			c.setDeuda(0);
			clienteDao.init();
			clienteDao.persist(c);
			clienteDao.commit();
			r.setData(c);
			r.setMessages("El cliente ha sido creado correctamente");
			r.setReason("");
			r.setSuccess(true);
		} catch (Exception e) {
			clienteDao.close();
			r.setData(null);
			r.setMessages("Error al persistir el cliente");
			r.setReason(e.getMessage());
			r.setSuccess(false);
		}
		return r;
	}
	

	
	public Respuesta<Cliente> modificar(String ruc, Cliente c){
		Respuesta<Cliente> r = new Respuesta<Cliente>();
		try {
			clienteDao.init();
			Cliente nuevoCliente = clienteDao.findById(ruc);
			if(nuevoCliente == null){
				r.setMessages("Error al modificar el cliente");
				r.setReason("No existe el Cliente");
				r.setSuccess(false);
			}else{
				nuevoCliente.setNombre(c.getNombre());
				clienteDao.update(nuevoCliente);
				clienteDao.commit();
				r.setMessages("El cliente ha sido modificado correctamente");
				r.setReason("");
				r.setSuccess(true);
			}
			r.setData(nuevoCliente);
		} catch (Exception e) {
			clienteDao.close();
			r.setData(null);
			r.setMessages("Error al modificar el cliente");
			r.setReason(e.getMessage());
			r.setSuccess(false);
		}
		return r;
	}
	
	public Respuesta<Cliente> buscarPorId(String ruc){
		Respuesta<Cliente> r = new Respuesta<Cliente>();
		try {
			clienteDao.init();
			Cliente nuevoCliente = clienteDao.findById(ruc);
			clienteDao.commit();
			r.setData(nuevoCliente);
			if(nuevoCliente == null)
				r.setMessages("No se encuentra el cliente");
			r.setReason("");
			r.setSuccess(nuevoCliente != null);
		} catch (Exception e) {
			clienteDao.close();
			r.setData(null);
			r.setMessages("Error en la base de datos");
			r.setReason(e.getMessage());
			r.setSuccess(false);
		}
		return r;
	}
	
	public Respuesta<Cliente> eliminar(String ruc){
		Respuesta<Cliente> r = new Respuesta<Cliente>();
		try {
			clienteDao.init();
			Cliente nuevoCliente = clienteDao.findById(ruc);
			
			if(nuevoCliente == null){
				r.setMessages("No se encuentra el cliente");
			}else{				
				nuevoCliente.setActivo(false);
				clienteDao.update(nuevoCliente);
			}
			clienteDao.commit();
			r.setData(nuevoCliente);
			r.setReason("");
			r.setSuccess(nuevoCliente != null);
		} catch (Exception e) {
			clienteDao.close();
			r.setData(null);
			r.setMessages("Error en la base de datos");
			r.setReason(e.getMessage());
			r.setSuccess(false);
		}
		return r;
	}
	
	public Respuesta<List<Cliente>> listarTodos(){
		Respuesta<List<Cliente>> r = new Respuesta<List<Cliente>>();
		try {
			clienteDao.init();
			List<Cliente> data = clienteDao.findAll();
			clienteDao.commit();
			if(data == null){
				r.setMessages("La base de datos esta vacia");
				r.setSuccess(false);
			}else{
				r.setMessages("");
				r.setSuccess(true);		
			}
			r.setData(data);
		} catch (Exception e) {
			clienteDao.close();
			r.setData(null);
			r.setMessages("Error en la base de datos");
			r.setReason(e.getMessage());
			r.setSuccess(false);
		}
		return r;
	}
	

}
