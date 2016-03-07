package py.pol.una.ii.pw.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import py.pol.una.ii.pw.model.Proveedor;
import py.pol.una.ii.pw.util.Respuesta;

@Stateless
public class ProveedorEjb {
	
	@PersistenceContext
	private EntityManager em;
	
	public Respuesta<Proveedor> nuevo(Proveedor p){
		Respuesta<Proveedor> r = new Respuesta<Proveedor>();
		try {
			p.setActivo(true);
			em.persist(p);
			r.setData(p);
			r.setMessages("El proveedor ha sido creado correctamente");
			r.setReason("");
			r.setSuccess(true);
		} catch (Exception e) {
			r.setData(null);
			r.setMessages("Error al persistir el proveedor");
			r.setReason(e.getMessage());
			r.setSuccess(false);
		}
		return r;
	}
	
	public Respuesta<Proveedor> modificar(String ruc, Proveedor p){
		Respuesta<Proveedor> r = new Respuesta<Proveedor>();
		try {
			Proveedor nuevoProveedor = this.findById(ruc);
			if(nuevoProveedor == null){
				r.setMessages("Error al modificar el proveedor");
				r.setReason("No existe el Proveedor");
				r.setSuccess(false);
			}else{
				nuevoProveedor.setNombre(p.getNombre());
				em.persist(nuevoProveedor);
				r.setMessages("El proveedor ha sido modificado correctamente");
				r.setReason("");
				r.setSuccess(true);
			}
			r.setData(nuevoProveedor);
		} catch (Exception e) {
			r.setData(null);
			r.setMessages("Error al modificar el proveedor");
			r.setReason(e.getMessage());
			r.setSuccess(false);
		}
		return r;
	}
	
	public Respuesta<Proveedor> buscarPorId(String ruc){
		Respuesta<Proveedor> r = new Respuesta<Proveedor>();
		try {
			Proveedor nuevoProveedor = this.findById(ruc);
			r.setData(nuevoProveedor);
			if(nuevoProveedor == null)
				r.setMessages("No se encuentra el proveedor");
			r.setReason("");
			r.setSuccess(nuevoProveedor != null);
		} catch (Exception e) {
			r.setData(null);
			r.setMessages("Error en la base de datos");
			r.setReason(e.getMessage());
			r.setSuccess(false);
		}
		return r;
	}
	
	public Respuesta<Proveedor> eliminar(String ruc){
		Respuesta<Proveedor> r = new Respuesta<Proveedor>();
		try {
			Proveedor nuevoProveedor = this.findById(ruc);
			if(nuevoProveedor == null){
				r.setMessages("No se encuentra el proveedor");
			}else{				
				em.persist(nuevoProveedor);
				nuevoProveedor.setActivo(false);
			}
			r.setData(nuevoProveedor);
			r.setReason("");
			r.setSuccess(nuevoProveedor != null);
		} catch (Exception e) {
			r.setData(null);
			r.setMessages("Error en la base de datos");
			r.setReason(e.getMessage());
			r.setSuccess(false);
		}
		return r;
	}
	
	public Respuesta<List<Proveedor>> listarTodos(){
		Respuesta<List<Proveedor>> r = new Respuesta<List<Proveedor>>();
		try {
			List<Proveedor> data = this.findAll();
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
	
	private Proveedor findById(String ruc){
		TypedQuery<Proveedor> query = em.createQuery(
				"SELECT p FROM Proveedor p WHERE p.ruc = :ruc AND p.activo = true", Proveedor.class);
		query.setParameter("ruc", ruc);
		List<Proveedor> e = query.getResultList();
		if(e.size() > 0) {
			return e.get(0);			
		}
		return null;
	}
	
	private List<Proveedor> findAll(){
		TypedQuery<Proveedor> query = em.createQuery(
				"SELECT p FROM Proveedor p WHERE p.activo = true", Proveedor.class);
		List<Proveedor> e = query.getResultList();
		if(e.size() > 0) {
			return e;			
		}
		return null;
	}
}
