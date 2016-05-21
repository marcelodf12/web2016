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

import py.pol.una.ii.pw.model.Cliente;
import py.pol.una.ii.pw.service.ClienteEjb;
import py.pol.una.ii.pw.util.Respuesta;
import py.pol.una.ii.py.annotation.Secured;

@Path("/cliente")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ClienteRest {
	
	@Inject
	private ClienteEjb clienteEjb;
	
	@GET
	@Path("/{ruc}")
	@Secured
	public Respuesta<Cliente> buscar(@PathParam("ruc") String ruc){
		return clienteEjb.buscarPorId(ruc);
	}
	
	@GET
	public Respuesta<List<Cliente>> buscarTodos(){
		return clienteEjb.listarTodos();
	}
	
	@POST
	public Respuesta<Cliente> alta(Cliente p){
		return clienteEjb.nuevo(p);
	}
	
	@PUT
	@Path("/{ruc}")
	public Respuesta<Cliente> modificar(@PathParam("ruc") String ruc, Cliente p){
		return clienteEjb.modificar(ruc, p);
	}
	
	@DELETE
	@Path("/{ruc}")
	public Respuesta<Cliente> baja(@PathParam("ruc") String ruc){
		return clienteEjb.eliminar(ruc);
	}
}

