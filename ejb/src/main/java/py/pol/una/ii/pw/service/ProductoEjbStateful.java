package py.pol.una.ii.pw.service;

import java.util.Iterator;

import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import py.pol.una.ii.pw.model.Producto;

@Stateful
public class ProductoEjbStateful {
	
	@PersistenceContext
	private EntityManager em;
	
	
	private Iterator<Producto> iterator;
	
	public void iniciar(){
		TypedQuery<Producto> query = em.createQuery(
				"SELECT p FROM Producto p WHERE p.activo = true", Producto.class);
		this.iterator = query.getResultList().iterator();
	}
	
	public Producto nextProducto(){
		Producto p = iterator.next();
		return p;
	}
	
	public boolean hasNext(){
		return iterator.hasNext();
	}
	
	@Remove
	public void terminar(){
		System.out.println("fin");
	}
	

}
