package py.pol.una.ii.pw.rest;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import py.pol.una.ii.pw.model.Producto;
import py.pol.una.ii.pw.service.ProductoEjb;
import py.pol.una.ii.pw.util.Respuesta;

@Path("/producto")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProductoRest {
	
	@Inject
	private ProductoEjb productoEjb;
	
	@GET
	@Path("/{id:[0-9][0-9]*}")
	public Respuesta<Producto> buscar(@PathParam("id") Long id){
		return productoEjb.buscarPorId(id);
	}
	
	@GET
	public Respuesta<List<Producto>> buscarTodos(){
		return productoEjb.listarTodos();
	}
	
	@POST
	public Respuesta<Producto> alta(Producto p){
		return productoEjb.nuevo(p);
	}
	
	@PUT
	@Path("/{id:[0-9][0-9]*}")
	public Respuesta<Producto> modificar(@PathParam("id") Long id, Producto p){
		return productoEjb.modificar(id, p);
	}
	
	@DELETE
	@Path("/{id:[0-9][0-9]*}")
	public Respuesta<Producto> baja(@PathParam("id") Long id){
		return productoEjb.eliminar(id);
	}
}

