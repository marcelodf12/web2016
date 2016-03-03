package py.pol.una.ii.pw.rest;

import java.util.ArrayList;

import javax.inject.Singleton;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import py.pol.una.ii.pw.model.Producto;

@Path("/productos")
@Singleton
public class ProductoRest {
	
	private static ArrayList<Producto> bd = new ArrayList<Producto>();
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Boolean add(Producto producto){
		bd.add(producto);
		return true;
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{id:[0-9][0-9]*}")
	public Producto buscar(Integer id){
		for(Producto o: bd){
			if(o.getId() == id)
				return o;
		}
		return null;
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Producto> todos(){
		return bd;
	}
	
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{id:[0-9][0-9]*}")
	public Boolean eliminar(@PathParam("id") Integer id){
		Producto producto = this.buscar(id);
		for(Producto o: bd){
			if(o.getId() == producto.getId())
				bd.remove(o);
		}
		return true;
	}
	
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{id:[0-9][0-9]*}")
	public Boolean modificar(@PathParam("id") Integer id, Producto producto){
		this.eliminar(id);
		this.add(producto);
		return true;
	}

}

