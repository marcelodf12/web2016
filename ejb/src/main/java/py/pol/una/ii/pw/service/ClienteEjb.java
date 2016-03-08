package py.pol.una.ii.pw.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import py.pol.una.ii.pw.model.Cliente;
import py.pol.una.ii.pw.util.Respuesta;

@Stateless
public class ClienteEjb {
	
	@PersistenceContext
	private EntityManager em;
	
	public Respuesta<Cliente> nuevo(Cliente c){
		Respuesta<Cliente> r = new Respuesta<Cliente>();
		try {
			c.setActivo(true);
			c.setDeuda(0);
			em.persist(c);
			r.setData(c);
			r.setMessages("El cliente ha sido creado correctamente");
			r.setReason("");
			r.setSuccess(true);
		} catch (Exception e) {
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
			Cliente nuevoCliente = this.findById(ruc);
			if(nuevoCliente == null){
				r.setMessages("Error al modificar el cliente");
				r.setReason("No existe el Cliente");
				r.setSuccess(false);
			}else{
				nuevoCliente.setNombre(c.getNombre());
				em.persist(nuevoCliente);
				r.setMessages("El cliente ha sido modificado correctamente");
				r.setReason("");
				r.setSuccess(true);
			}
			r.setData(nuevoCliente);
		} catch (Exception e) {
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
			Cliente nuevoCliente = this.findById(ruc);
			r.setData(nuevoCliente);
			if(nuevoCliente == null)
				r.setMessages("No se encuentra el cliente");
			r.setReason("");
			r.setSuccess(nuevoCliente != null);
		} catch (Exception e) {
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
			Cliente nuevoCliente = this.findById(ruc);
			if(nuevoCliente == null){
				r.setMessages("No se encuentra el cliente");
			}else{				
				em.persist(nuevoCliente);
				nuevoCliente.setActivo(false);
			}
			r.setData(nuevoCliente);
			r.setReason("");
			r.setSuccess(nuevoCliente != null);
		} catch (Exception e) {
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
			List<Cliente> data = this.findAll();
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
	
	private Cliente findById(String ruc){
		TypedQuery<Cliente> query = em.createQuery(
				"SELECT c FROM Cliente c WHERE c.ruc = :ruc AND c.activo = true", Cliente.class);
		query.setParameter("ruc", ruc);
		List<Cliente> e = query.getResultList();
		if(e.size() > 0) {
			return e.get(0);			
		}
		return null;
	}
	
	private List<Cliente> findAll(){
		TypedQuery<Cliente> query = em.createQuery(
				"SELECT c FROM Cliente c WHERE c.activo = true", Cliente.class);
		List<Cliente> e = query.getResultList();
		if(e.size() > 0) {
			return e;			
		}
		return null;
	}
}
