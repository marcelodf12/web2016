package py.pol.una.ii.pw.rest;

import java.util.ArrayList;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import py.pol.una.ii.pw.model.Venta;
import py.pol.una.ii.pw.model.Cliente;
import py.pol.una.ii.pw.model.Detalle;
import py.pol.una.ii.pw.model.Producto;

@Path("/ventas")
@Singleton
public class VentaRest {
	
	private static ArrayList<Venta> bd = new ArrayList<Venta>();
	
	@Inject
	private ProductoRest productoRepo;
	
	@Inject
	private ClienteRest clienteRepo;
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Boolean add(Venta venta){
		Long total = (long) 0;
		for(Detalle d: venta.getDetalles()){
			total += d.getPrecio() * d.getCantidad();
			Producto p = productoRepo.buscar(d.getProducto().getId());
			p.setStock(p.getStock()-d.getCantidad());
			productoRepo.modificar(p.getId(), p);
		}
		Cliente c = clienteRepo.buscar(venta.getCliente().getId());
		c.setDeuda(c.getDeuda() + total);
		clienteRepo.modificar(c.getId(), c);
		venta.setTotal(total);
		bd.add(venta);
		return true;
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{id:[0-9][0-9]*}")
	public Venta buscar(Integer id){
		for(Venta o: bd){
			if(o.getId() == id)
				return o;
		}
		return null;
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Venta> todos(){
		return bd;
	}

}

