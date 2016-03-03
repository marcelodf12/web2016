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

import py.pol.una.ii.pw.model.Cliente;
import py.pol.una.ii.pw.model.Pago;

@Path("/clientes")
@Singleton
public class ClienteRest {
	
	private static ArrayList<Cliente> bd = new ArrayList<Cliente>();
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Boolean add(Cliente cliente){
		bd.add(cliente);
		return true;
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{id:[0-9][0-9]*}")
	public Cliente buscar(Integer id){
		for(Cliente o: bd){
			if(o.getId() == id)
				return o;
		}
		return null;
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Cliente> todos(){
		return bd;
	}
	
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{id:[0-9][0-9]*}")
	public Boolean eliminar(@PathParam("id") Integer id){
		Cliente cliente = this.buscar(id);
		for(Cliente o: bd){
			if(o.getId() == cliente.getId())
				bd.remove(o);
		}
		return true;
	}
	
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{id:[0-9][0-9]*}")
	public Boolean modificar(@PathParam("id") Integer id, Cliente cliente){
		this.eliminar(id);
		this.add(cliente);
		return true;
	}
	
	@POST
	@Path("/{id:[0-9][0-9]*}/pagos")
	@Produces(MediaType.APPLICATION_JSON)
	public Boolean nuevoPago(@PathParam("id") Integer id, Long monto){
		Cliente c = this.buscar(id);
		c.getPagos().add(new Pago(monto));
		c.setDeuda(c.getDeuda() - monto);
		this.modificar(id, c);
		return true;
	}
	
	

}
