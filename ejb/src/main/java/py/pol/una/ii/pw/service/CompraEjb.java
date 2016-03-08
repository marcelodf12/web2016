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

import py.pol.una.ii.pw.dto.CompraDetalleDto;
import py.pol.una.ii.pw.dto.CompraDto;
import py.pol.una.ii.pw.model.Compra;
import py.pol.una.ii.pw.model.CompraDetalle;
import py.pol.una.ii.pw.util.Respuesta;

@Stateless
public class CompraEjb {
	
	@PersistenceContext
	private EntityManager em;
		
	@Inject
	private ProductoEjb productoEjb;
	
	@Inject
	private ProveedorEjb proveedorEjb;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Respuesta<Compra> alta(CompraDto compraDto){
		Respuesta<Compra> r = new Respuesta<Compra>();
		try {
			Compra c = this.nuevaCompra(compraDto);
			if(c == null){
				r.setMessages("La compra no se puede persistir");
				r.setReason("No existe un producto del detalle");
				r.setSuccess(false);
				r.setData(null);
				return r;
			}
				
			c.setProveedor(proveedorEjb.buscarPorId(compraDto.getRucProveedor()).getData());
			if(c.getProveedor()==null){
				r.setMessages("La compra no se puede persistir");
				r.setReason("No existe el proveedor");
				r.setSuccess(false);
				r.setData(null);
			}else{
				em.persist(c);
				r.setSuccess(true);
				r.setMessages("Compra guardada correctamente");
				r.setData(c);
			}
		} catch (Exception e) {
			r.setData(null);
			r.setMessages("Error al persistir la compra");
			r.setReason(e.getMessage());
		}
		return r;
	}
	
	
	public Respuesta<List<Compra>> listarTodos(){
		Respuesta<List<Compra>> r = new Respuesta<List<Compra>>();
		try {
			List<Compra> data = this.findAll();
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
	
	private List<Compra> findAll(){
		TypedQuery<Compra> query = em.createQuery(
				"SELECT c FROM Compra c", Compra.class);
		List<Compra> e = query.getResultList();
		if(e.size() > 0) {
			return e;			
		}
		return null;
	}
	
	
	
	private Compra nuevaCompra(CompraDto compraDto){
		Compra compra = new Compra(compraDto);
		Integer montoTotal = 0;
		compra.setFecha(new Date());
		for(CompraDetalleDto det : compraDto.getCompraDetalles()){
			montoTotal += det.getPrecio() * det.getCantidad();
			CompraDetalle nuevoDet = new CompraDetalle();
			nuevoDet.setCantidad(det.getCantidad());
			nuevoDet.setPrecio(det.getPrecio());
			nuevoDet.setProducto(productoEjb.buscarPorId(det.getIdProducto()).getData());
			if(nuevoDet.getProducto()==null)
				return null;
			compra.getCompraDetalles().add(nuevoDet);
			nuevoDet.setCompra(compra);
		}
		compra.setMontoTotal(montoTotal);
		return compra;
	}

}
