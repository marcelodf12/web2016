package py.pol.una.ii.pw.rest;

import java.util.ArrayList;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import py.pol.una.ii.pw.model.Compra;
import py.pol.una.ii.pw.model.Detalle;
import py.pol.una.ii.pw.model.Producto;


@Path("/compras")
@Singleton
public class CompraRest {
	
	private static ArrayList<Compra> bd = new ArrayList<Compra>();
	
	@Inject
	private ProductoRest productoRepo;
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Boolean add(Compra compra){
		Long total = (long) 0;
		for(Detalle d: compra.getDetalles()){
			total += d.getPrecio() * d.getCantidad();
			Producto p = productoRepo.buscar(d.getProducto().getId());
			p.setStock(p.getStock()+d.getCantidad());
			productoRepo.modificar(p.getId(), p);
		}
		compra.setTotal(total);
		bd.add(compra);
		return true;
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{id:[0-9][0-9]*}")
	public Compra buscar(Integer id){
		for(Compra o: bd){
			if(o.getId() == id)
				return o;
		}
		return null;
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Compra> todos(){
		return bd;
	}

}
