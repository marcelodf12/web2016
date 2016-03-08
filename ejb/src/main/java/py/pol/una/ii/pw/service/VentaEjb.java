package py.pol.una.ii.pw.service;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import py.pol.una.ii.pw.dto.VentaDetalleDto;
import py.pol.una.ii.pw.dto.VentaDto;
import py.pol.una.ii.pw.model.Venta;
import py.pol.una.ii.pw.model.VentaDetalle;
import py.pol.una.ii.pw.util.Respuesta;

@Stateless
public class VentaEjb {
	
	@PersistenceContext
	private EntityManager em;
		
	@Inject
	private ProductoEjb productoEjb;
	
	@Inject
	private ClienteEjb clienteEjb;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Respuesta<Venta> alta(VentaDto ventaDto){
		Respuesta<Venta> r = new Respuesta<Venta>();
		try {
			Venta c = this.nuevaVenta(ventaDto);
			if(c == null){
				r.setMessages("La venta no se puede persistir");
				r.setReason("No existe un producto del detalle");
				r.setSuccess(false);
				r.setData(null);
				return r;
			}
				
			c.setCliente(clienteEjb.buscarPorId(ventaDto.getRucCliente()).getData());
			if(c.getCliente()==null){
				r.setMessages("La venta no se puede persistir");
				r.setReason("No existe el cliente");
				r.setSuccess(false);
				r.setData(null);
			}else{
				c.getCliente().setDeuda(c.getCliente().getDeuda() + c.getMontoTotal());
				em.persist(c);
				r.setSuccess(true);
				r.setMessages("Venta guardada correctamente");
				r.setData(c);
			}
		} catch (Exception e) {
			r.setData(null);
			r.setMessages("Error al persistir la venta");
			r.setReason(e.getMessage());
		}
		return r;
	}
	
	
	public Respuesta<List<Venta>> listarTodos(){
		Respuesta<List<Venta>> r = new Respuesta<List<Venta>>();
		try {
			List<Venta> data = this.findAll();
			if(data == null){
				r.setMessages("La base de datos esta vacia");
				r.setSuccess(false);
			}else{
				r.setMessages("");
				r.setSuccess(true);		
			}
			r.setData(data);
		} catch (Exception e) {
			r.setData(null);
			r.setMessages("Error en la base de datos");
			r.setReason(e.getMessage());
			r.setSuccess(false);
		}
		return r;
	}
	
	private List<Venta> findAll(){
		TypedQuery<Venta> query = em.createQuery(
				"SELECT c FROM Venta c", Venta.class);
		List<Venta> e = query.getResultList();
		if(e.size() > 0) {
			return e;			
		}
		return null;
	}
	
	
	
	private Venta nuevaVenta(VentaDto ventaDto){
		Venta venta = new Venta(ventaDto);
		Integer montoTotal = 0;
		venta.setFecha(new Date());
		for(VentaDetalleDto det : ventaDto.getVentaDetalles()){
			montoTotal += det.getPrecio() * det.getCantidad();
			VentaDetalle nuevoDet = new VentaDetalle();
			nuevoDet.setCantidad(det.getCantidad());
			nuevoDet.setPrecio(det.getPrecio());
			nuevoDet.setProducto(productoEjb.buscarPorId(det.getIdProducto()).getData());
			if(nuevoDet.getProducto()==null)
				return null;
			venta.getVentaDetalles().add(nuevoDet);
			nuevoDet.setVenta(venta);
		}
		venta.setMontoTotal(montoTotal);
		return venta;
	}

}
