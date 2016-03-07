package py.pol.una.ii.pw.rest;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
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
}

