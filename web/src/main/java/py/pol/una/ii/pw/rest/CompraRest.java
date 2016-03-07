package py.pol.una.ii.pw.rest;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;

import py.pol.una.ii.pw.dto.CompraDto;
import py.pol.una.ii.pw.model.Compra;
import py.pol.una.ii.pw.service.CompraEjb;
import py.pol.una.ii.pw.util.Respuesta;

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
