package py.pol.una.ii.pw.rest;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;

import py.pol.una.ii.pw.dto.VentaDto;
import py.pol.una.ii.pw.model.Venta;
import py.pol.una.ii.pw.service.VentaEjb;
import py.pol.una.ii.pw.util.Respuesta;

public class VentaRest {
	
	@Inject
	private VentaEjb ventaEjb;
	
	@GET
	public Respuesta<List<Venta>> buscarTodos(){
		return ventaEjb.listarTodos();
	}
	
	@POST
	public Respuesta<Venta> alta(VentaDto c){
		return ventaEjb.alta(c);
	}

}