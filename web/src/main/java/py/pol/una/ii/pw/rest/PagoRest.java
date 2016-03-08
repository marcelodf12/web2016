package py.pol.una.ii.pw.rest;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import py.pol.una.ii.pw.dto.PagoDto;
import py.pol.una.ii.pw.model.Pago;
import py.pol.una.ii.pw.service.PagoEjb;
import py.pol.una.ii.pw.util.Respuesta;

@Path("/pago")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PagoRest {
	
	@Inject
	private PagoEjb pagoEjb;
	
	@GET
	public Respuesta<List<Pago>> buscarTodos(){
		return pagoEjb.listarTodos();
	}
	
	@POST
	public Respuesta<Pago> alta(PagoDto p){
		return pagoEjb.alta(p);
	}

}
