package py.pol.una.ii.pw.service;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import py.pol.una.ii.pw.dao.ProveedorDAO;
import py.pol.una.ii.pw.model.Proveedor;
import py.pol.una.ii.pw.util.Respuesta;

@Stateless
public class ProveedorEjb {
	
	@EJB
	private ProveedorDAO proveedorDao;
	
	
	public Respuesta<Proveedor> nuevo(Proveedor p){
		Respuesta<Proveedor> r = new Respuesta<Proveedor>();
		try {
			p.setActivo(true);
			proveedorDao.init();
			proveedorDao.persist(p);
			r.setData(p);
			r.setMessages("El proveedor ha sido creado correctamente");
			r.setReason("");
			r.setSuccess(true);
			proveedorDao.commit();
		} catch (Exception e) {
			r.setData(null);
			r.setMessages("Error al persistir el proveedor");
			r.setReason(e.getMessage());
			r.setSuccess(false);
			proveedorDao.close();
		}
		return r;
	}
	
	public Respuesta<Proveedor> modificar(String ruc, Proveedor p){
		Respuesta<Proveedor> r = new Respuesta<Proveedor>();
		try {
			proveedorDao.init();
			Proveedor nuevoProveedor = proveedorDao.findById(ruc);
			if(nuevoProveedor == null){
				r.setMessages("Error al modificar el proveedor");
				r.setReason("No existe el Proveedor");
				r.setSuccess(false);
			}else{
				nuevoProveedor.setNombre(p.getNombre());
				proveedorDao.update(nuevoProveedor);
				r.setMessages("El proveedor ha sido modificado correctamente");
				r.setReason("");
				r.setSuccess(true);
			}
			r.setData(nuevoProveedor);
			proveedorDao.commit();
		} catch (Exception e) {
			r.setData(null);
			r.setMessages("Error al modificar el proveedor");
			r.setReason(e.getMessage());
			r.setSuccess(false);
			proveedorDao.close();
		}
		return r;
	}
	
	public Respuesta<Proveedor> buscarPorId(String ruc){
		Respuesta<Proveedor> r = new Respuesta<Proveedor>();
		try {
			proveedorDao.init();
			Proveedor nuevoProveedor = proveedorDao.findById(ruc);
			r.setData(nuevoProveedor);
			if(nuevoProveedor == null)
				r.setMessages("No se encuentra el proveedor");
			r.setReason("");
			r.setSuccess(nuevoProveedor != null);
			proveedorDao.commit();
		} catch (Exception e) {
			r.setData(null);
			r.setMessages("Error en la base de datos");
			r.setReason(e.getMessage());
			r.setSuccess(false);
			proveedorDao.close();
		}
		return r;
	}
	
	public Respuesta<Proveedor> eliminar(String ruc){
		Respuesta<Proveedor> r = new Respuesta<Proveedor>();
		try {
			proveedorDao.init();
			Proveedor nuevoProveedor = proveedorDao.findById(ruc);
			if(nuevoProveedor == null){
				r.setMessages("No se encuentra el proveedor");
			}else{				
				proveedorDao.persist(nuevoProveedor);
				nuevoProveedor.setActivo(false);
			}
			r.setData(nuevoProveedor);
			r.setReason("");
			r.setSuccess(nuevoProveedor != null);
			proveedorDao.commit();
		} catch (Exception e) {
			r.setData(null);
			r.setMessages("Error en la base de datos");
			r.setReason(e.getMessage());
			r.setSuccess(false);
			proveedorDao.close();
		}
		return r;
	}
	
	public Respuesta<List<Proveedor>> listarTodos(){
		Respuesta<List<Proveedor>> r = new Respuesta<List<Proveedor>>();
		try {
			proveedorDao.init();
			List<Proveedor> data = proveedorDao.findAll();
			if(data == null){
				r.setMessages("La base de datos esta vacia");
				r.setSuccess(false);
			}else{
				r.setMessages("");
				r.setSuccess(true);		
			}
			r.setData(data);
			proveedorDao.commit();
		} catch (Exception e) {
			r.setData(null);
			r.setMessages("Error en la base de datos");
			r.setReason(e.getMessage());
			r.setSuccess(false);
			proveedorDao.close();
		}
		return r;
	}
	

}
