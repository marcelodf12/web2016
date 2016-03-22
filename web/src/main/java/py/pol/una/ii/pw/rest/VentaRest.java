package py.pol.una.ii.pw.rest;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import py.pol.una.ii.pw.dto.VentaDto;
import py.pol.una.ii.pw.model.Venta;
import py.pol.una.ii.pw.service.VentaEjb;
import py.pol.una.ii.pw.util.Respuesta;

@Path("/venta")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class VentaRest {
	
	@Inject
	private VentaEjb ventaEjb;
	
	@GET
	public Respuesta<List<Venta>> buscarTodos(){
		return ventaEjb.listarTodos();
	}
	
	@POST
	public Respuesta<Venta> alta(VentaDto c){
		Respuesta<Venta> r = new Respuesta<Venta>();
		try {
			return ventaEjb.alta(c);
		} catch (Exception e) {
			r.setData(null);
			r.setMessages("No hay stock suficiente");
			r.setReason(e.getMessage());
			return r;
		}
		
	}

}