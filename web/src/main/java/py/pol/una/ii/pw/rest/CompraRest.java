package py.pol.una.ii.pw.rest;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import py.pol.una.ii.pw.dto.CompraDto;
import py.pol.una.ii.pw.model.Compra;
import py.pol.una.ii.pw.service.CompraEjb;
import py.pol.una.ii.pw.util.Respuesta;

@Path("/compra")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CompraRest {
	
	@Inject
	private CompraEjb compraEjb;
	
	@GET
	public Respuesta<List<Compra>> buscarTodos(){
		return compraEjb.listarTodos();
	}
	
	@POST
	public Respuesta<Compra> alta(CompraDto c){
		return compraEjb.alta(c);
	}

}
