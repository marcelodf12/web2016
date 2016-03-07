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

import py.pol.una.ii.pw.model.Proveedor;
import py.pol.una.ii.pw.service.ProveedorEjb;
import py.pol.una.ii.pw.util.Respuesta;

@Path("/proveedor")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProveedorRest {
	
	@Inject
	private ProveedorEjb proveedorEjb;
	
	@GET
	@Path("/{ruc}")
	public Respuesta<Proveedor> buscar(@PathParam("ruc") String ruc){
		return proveedorEjb.buscarPorId(ruc);
	}
	
	@GET
	public Respuesta<List<Proveedor>> buscarTodos(){
		return proveedorEjb.listarTodos();
	}
	
	@POST
	public Respuesta<Proveedor> alta(Proveedor p){
		return proveedorEjb.nuevo(p);
	}
	
	@PUT
	@Path("/{ruc}")
	public Respuesta<Proveedor> modificar(@PathParam("ruc") String ruc, Proveedor p){
		return proveedorEjb.modificar(ruc, p);
	}
	
	@DELETE
	@Path("/{ruc}")
	public Respuesta<Proveedor> baja(@PathParam("ruc") String ruc){
		return proveedorEjb.eliminar(ruc);
	}
}

