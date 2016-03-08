package py.pol.una.ii.pw.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import py.pol.una.ii.pw.dto.PagoDto;
import py.pol.una.ii.pw.model.Pago;
import py.pol.una.ii.pw.util.Respuesta;

@Stateless
public class PagoEjb {
	
	@Inject
	private ClienteEjb clienteEjb;
	
	@PersistenceContext
	private EntityManager em;
	
	public Respuesta<Pago> alta(PagoDto p){
		Respuesta<Pago> r = new Respuesta<Pago>();
		try {
			Pago pago = new Pago();
			pago.setCliente(clienteEjb.buscarPorId(p.getRucCliente()).getData());
			if(pago.getCliente()== null){
				r.setData(null);
				r.setMessages("Error al persistir el pago");
				r.setReason("No existe el cliente");
				r.setSuccess(false);
			}else{
				pago.setTotal(p.getTotal());
				pago.getCliente().setDeuda(pago.getCliente().getDeuda() - p.getTotal());
				em.persist(pago);
				r.setData(pago);
				r.setSuccess(true);
				r.setMessages("El pago ha sido registrado correctamente");
			}
		} catch (Exception e) {
			r.setData(null);
			r.setMessages("Error al persistir el pago");
			r.setReason(e.getMessage());
			r.setSuccess(false);
		}
		return r;
	}
	
	public Respuesta<List<Pago>> listarTodos(){
		Respuesta<List<Pago>> r = new Respuesta<List<Pago>>();
		try {
			List<Pago> data = this.findAll();
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
	
	private List<Pago> findAll(){
		TypedQuery<Pago> query = em.createQuery(
				"SELECT p FROM Pago p", Pago.class);
		List<Pago> e = query.getResultList();
		if(e.size() > 0) {
			return e;			
		}
		return null;
	}

}
