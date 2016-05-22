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
		System.out.println("inicio");
		TypedQuery<Producto> query = em.createQuery(
				"SELECT p FROM Producto p WHERE p.activo = true", Producto.class);
		this.iterator = query.getResultList().iterator();
	}
	
	public Producto nextProducto(){
		System.out.print("++++ ");
		Producto p = iterator == null? null: iterator.next();
		System.out.println(p);
		return p;
	}
	
	public boolean hasNext(){
		System.out.print("- ");
		boolean r = iterator==null? false : iterator.hasNext();
		System.out.println(r);
		return r;
	}
	
	@Remove
	public void terminar(){
		System.out.println("fin");
	}
	

}
