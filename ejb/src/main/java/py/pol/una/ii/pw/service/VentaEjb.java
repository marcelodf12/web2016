package py.pol.una.ii.pw.service;

import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import py.pol.una.ii.pw.dao.VentaDAO;
import py.pol.una.ii.pw.dao.ProductoDAO;
import py.pol.una.ii.pw.dao.ClienteDAO;
import py.pol.una.ii.pw.dto.VentaDetalleDto;
import py.pol.una.ii.pw.dto.VentaDto;
import py.pol.una.ii.pw.model.Venta;
import py.pol.una.ii.pw.model.VentaDetalle;
import py.pol.una.ii.pw.model.Producto;
import py.pol.una.ii.pw.model.Cliente;
import py.pol.una.ii.pw.util.Respuesta;

@Stateless
public class VentaEjb {
	
	@PersistenceContext
	private EntityManager em;
	
	@EJB
	private VentaDAO ventaDao;
	
	@EJB
	private ClienteDAO clienteDao;
	
	@EJB
	private ProductoDAO productoDao;
	
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
				
			
			if(c.getCliente()==null){
				r.setMessages("La venta no se puede persistir");
				r.setReason("No existe el cliente");
				r.setSuccess(false);
				r.setData(null);
			}else{
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
		clienteDao.init();
		Cliente cliente = clienteDao.findById(ventaDto.getRucCliente());
		venta.setCliente(cliente);
		Integer montoTotal = 0;
		venta.setFecha(new Date());
		ventaDao.init();
		for(VentaDetalleDto det : ventaDto.getVentaDetalles()){
			montoTotal += det.getPrecio() * det.getCantidad();
			VentaDetalle nuevoDet = new VentaDetalle();
			nuevoDet.setCantidad(det.getCantidad());
			nuevoDet.setPrecio(det.getPrecio());	
			Producto p = ventaDao.findProducto(det.getIdProducto());
			if(p==null){
				return null;
			}else{
				p.setStock(p.getStock()-det.getCantidad());
				nuevoDet.setProducto(p);
				ventaDao.updateProducto(p);
			}
			venta.getVentaDetalles().add(nuevoDet);
			nuevoDet.setVenta(venta);
		}
		venta.setMontoTotal(montoTotal);
		ventaDao.persist(venta);
		ventaDao.commit();
		clienteDao.close();
		return venta;
	}

}
