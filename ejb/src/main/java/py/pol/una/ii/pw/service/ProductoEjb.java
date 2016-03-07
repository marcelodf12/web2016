package py.pol.una.ii.pw.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import py.pol.una.ii.pw.model.Producto;
import py.pol.una.ii.pw.util.Respuesta;

@Stateless
public class ProductoEjb {
	
	@PersistenceContext
	private EntityManager em;
	
	public Respuesta<Producto> nuevo(Producto p){
		Respuesta<Producto> r = new Respuesta<Producto>();
		try {
			p.setActivo(true);
			em.persist(p);
			r.setData(p);
			r.setMessages("El producto ha sido creado correctamente");
			r.setReason("");
			r.setSuccess(true);
		} catch (Exception e) {
			r.setData(null);
			r.setMessages("Error al persistir el producto");
			r.setReason(e.getMessage());
			r.setSuccess(false);
		}
		return r;
	}
	
	public Respuesta<Producto> modificar(Long id, Producto p){
		Respuesta<Producto> r = new Respuesta<Producto>();
		try {
			Producto nuevoProducto = this.findById(id);
			if(nuevoProducto == null){
				r.setMessages("Error al modificar el producto");
				r.setReason("No existe el Producto");
				r.setSuccess(false);
			}else{
				nuevoProducto.setNombre(p.getNombre());
				nuevoProducto.setPrecio(p.getPrecio());
				em.persist(nuevoProducto);
				r.setMessages("El producto ha sido modificado correctamente");
				r.setReason("");
				r.setSuccess(true);
			}
			r.setData(nuevoProducto);
		} catch (Exception e) {
			r.setData(null);
			r.setMessages("Error al modificar el producto");
			r.setReason(e.getMessage());
			r.setSuccess(false);
		}
		return r;
	}
	
	public Respuesta<Producto> buscarPorId(Long id){
		Respuesta<Producto> r = new Respuesta<Producto>();
		try {
			Producto nuevoProducto = this.findById(id);
			r.setData(nuevoProducto);
			if(nuevoProducto == null)
				r.setMessages("No se encuentra el producto");
			r.setReason("");
			r.setSuccess(nuevoProducto != null);
		} catch (Exception e) {
			r.setData(null);
			r.setMessages("Error en la base de datos");
			r.setReason(e.getMessage());
			r.setSuccess(false);
		}
		return r;
	}
	
	public Respuesta<Producto> eliminar(Long id){
		Respuesta<Producto> r = new Respuesta<Producto>();
		try {
			Producto nuevoProducto = this.findById(id);
			if(nuevoProducto == null){
				r.setMessages("No se encuentra el producto");
			}else{				
				em.persist(nuevoProducto);
				nuevoProducto.setActivo(false);
			}
			r.setData(nuevoProducto);
			r.setReason("");
			r.setSuccess(nuevoProducto != null);
		} catch (Exception e) {
			r.setData(null);
			r.setMessages("Error en la base de datos");
			r.setReason(e.getMessage());
			r.setSuccess(false);
		}
		return r;
	}
	
	public Respuesta<List<Producto>> listarTodos(){
		Respuesta<List<Producto>> r = new Respuesta<List<Producto>>();
		try {
			List<Producto> data = this.findAll();
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
	
	private Producto findById(Long id){
		TypedQuery<Producto> query = em.createQuery(
				"SELECT p FROM Producto p WHERE p.id = :id AND p.activo = true", Producto.class);
		query.setParameter("id", id);
		List<Producto> e = query.getResultList();
		if(e.size() > 0) {
			return e.get(0);			
		}
		return null;
	}
	
	private List<Producto> findAll(){
		TypedQuery<Producto> query = em.createQuery(
				"SELECT p FROM Producto p WHERE p.activo = true", Producto.class);
		List<Producto> e = query.getResultList();
		if(e.size() > 0) {
			return e;			
		}
		return null;
	}
}
