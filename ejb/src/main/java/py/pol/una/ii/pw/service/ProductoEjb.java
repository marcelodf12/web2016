package py.pol.una.ii.pw.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.EJBTransactionRolledbackException;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import py.pol.una.ii.pw.model.Producto;
import py.pol.una.ii.pw.model.ProductoDuplicado;
import py.pol.una.ii.pw.util.Pagina;
import py.pol.una.ii.pw.util.Respuesta;


@Stateless
public class ProductoEjb {
	
	@PersistenceContext
	private EntityManager em;
	
	@Resource
	private SessionContext context;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Respuesta<Producto> nuevo(Producto p){
		Respuesta<Producto> r = new Respuesta<Producto>();
		try {
			p.setActivo(true);
			// En vez de colocar este if hay que hacer un try:catch
			// Pero por no funciona porque la excepcion se lanza al final
			// de la transaccion cuando se trata de comitear
			if(this.findByName(p.getNombre())==null){
				this.persistirProducto(p);
				r.setData(p);
				r.setMessages("El producto ha sido creado correctamente");
				r.setReason("");
				r.setSuccess(true);
			}else{ // Todo este bloque else no seria necesario si
				   //funcionara el try:catch
				this.persistirProductoDuplicado(p.getNombre());
				throw new Exception("Producto duplicado");
			}
		} catch (EJBTransactionRolledbackException ex){
			this.persistirProductoDuplicado(p.getNombre());
			r.setData(null);
			r.setMessages("Error al persistir el producto");
			r.setReason(ex.getMessage());
			r.setSuccess(false);
		} catch (Exception e) {	
			r.setData(null);
			r.setMessages("Error al persistir el producto");
			r.setReason(e.getMessage());
			r.setSuccess(false);
		}
		return r;
	}
	
	
	//@TransactionAttribute(TransactionAttributeType.REQUIRED_NEW)
	public void persistirProducto(Producto p) throws EJBTransactionRolledbackException
	{
		em.persist(p);
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
	
	public Respuesta<Pagina<Producto>> listarTodos(Integer inicio, Integer cant){
		Respuesta<Pagina<Producto>> r = new Respuesta<Pagina<Producto>>();
		try {
			Pagina<Producto> data = this.findList(inicio, cant);
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
	
	public Producto findById(Long id){
		TypedQuery<Producto> query = em.createQuery(
				"SELECT p FROM Producto p WHERE p.id = :id AND p.activo = true", Producto.class);
		query.setParameter("id", id);
		List<Producto> e = query.getResultList();
		if(e.size() > 0) {
			return e.get(0);			
		}
		return null;
	}
	
	private Producto findByName(String nombre){
		TypedQuery<Producto> query = em.createQuery(
				"SELECT p FROM Producto p WHERE p.nombre = :nombre", Producto.class);
		query.setParameter("nombre", nombre);
		List<Producto> e = query.getResultList();
		if(e.size() > 0) {
			return e.get(0);			
		}
		return null;
	}
	
	private ProductoDuplicado findProductoDuplicado(Long id){
		TypedQuery<ProductoDuplicado> query = em.createQuery(
				"SELECT p FROM ProductoDuplicado p WHERE p.producto.id = :id", ProductoDuplicado.class);
		query.setParameter("id", id);
		List<ProductoDuplicado> e = query.getResultList();
		if(e.size() > 0) {
			return e.get(0);			
		}
		return null;
	}
	
//	private List<Producto> findAll(){
//		TypedQuery<Producto> query = em.createQuery(
//				"SELECT p FROM Producto p WHERE p.activo = true", Producto.class);
//		List<Producto> e = query.getResultList();
//		if(e.size() > 0) {
//			return e;			
//		}
//		return null;
//	}
	
	private Pagina<Producto> findList(Integer inicio, Integer cant){
		if(cant<=0)
			return null;
		if(cant>100)
			cant = 100;
		Pagina<Producto> productos = new Pagina<Producto>();
		TypedQuery<Producto> query = em.createQuery(
				"SELECT p FROM Producto p WHERE p.activo = true", Producto.class);
		TypedQuery<Long> queryCount = em.createQuery(
				"SELECT count(p) FROM Producto p WHERE p.activo = true", Long.class);
		Long total = (Long) queryCount.getSingleResult();
		List<Producto> e = query.setFirstResult(inicio).setMaxResults(cant).getResultList();
		if(e.size() > 0) {
			productos.setData(e);
			productos.setTotal(total);
			return productos;			
		}
		return null;
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public Respuesta<String> cargaMasiva(String file){
		Respuesta<String> r = new Respuesta<String>();
		Boolean fallo = false;
		ArrayList<Integer> errores = new ArrayList<Integer>();
		BufferedReader br = null;
		String line = "";
		Integer numeroDeLinea = 0;
		String nombre = null;
		try {
			br = new BufferedReader(new FileReader(file));
			while ((line = br.readLine()) != null) {
				System.out.println(" * " + line);
				try{
					numeroDeLinea++;
					String campos[] = line.split(";");
					if(campos.length==2){
						nombre = campos[0];
						Integer precio = Integer.valueOf(campos[1]);
						Producto p = new Producto();
						p.setActivo(true);
						p.setNombre(nombre);
						p.setPrecio(precio);
						Respuesta<Producto> r1 = this.nuevo(p);
						if(!r1.getSuccess()){
							errores.add(numeroDeLinea);
							fallo = true;
						}
					}else{
						errores.add(numeroDeLinea);
						fallo = true;
					}
				}catch(Exception e){

				}
			}
			
		} catch (Exception e) {
			fallo = true;
		} finally {
			if(br!=null)
				try {
					br.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		r.setSuccess(!fallo);
		if(fallo){
			context.setRollbackOnly();
			String mes = "ERRORES EN LAS LINEAS: ";
			for(Integer i: errores){
				mes += i.toString() + ", ";
			}
			r.setMessages(mes);
		}
		
		return r;
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	private void persistirProductoDuplicado (String nombre){
		Producto p1 = findByName(nombre);
		ProductoDuplicado pd = findProductoDuplicado(p1.getId());
		if(pd == null){	
			pd = new ProductoDuplicado();
			pd.setProducto(p1);
			pd.setCantidad(0);
		}
		pd.setCantidad(pd.getCantidad()+1);
		em.persist(pd);
	}


}
