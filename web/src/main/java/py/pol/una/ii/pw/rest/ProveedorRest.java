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

import py.pol.una.ii.pw.model.Proveedor;

@Path("/proveedores")
@Singleton
public class ProveedorRest {
	
	private static ArrayList<Proveedor> bd = new ArrayList<Proveedor>();
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Boolean add(Proveedor proveedor){
		bd.add(proveedor);
		return true;
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{id:[0-9][0-9]*}")
	public Proveedor buscar(Integer id){
		for(Proveedor o: bd){
			if(o.getId() == id)
				return o;
		}
		return null;
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Proveedor> todos(){
		return bd;
	}
	
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{id:[0-9][0-9]*}")
	public Boolean eliminar(@PathParam("id") Integer id){
		Proveedor proveedor = this.buscar(id);
		for(Proveedor o: bd){
			if(o.getId() == proveedor.getId())
				bd.remove(o);
		}
		return true;
	}
	
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{id:[0-9][0-9]*}")
	public Boolean modificar(@PathParam("id") Integer id){
		Proveedor proveedor = this.buscar(id);
		this.eliminar(id);
		this.add(proveedor);
		return true;
	}

}

