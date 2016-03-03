package py.pol.una.ii.pw.model;

import java.util.ArrayList;

public class Venta {
	private Integer id;
	private Cliente cliente;
	private ArrayList<Detalle> detalles;
	private Long total;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Cliente getCliente() {
		return cliente;
	}
	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
	public ArrayList<Detalle> getDetalles() {
		return detalles;
	}
	public void setDetalles(ArrayList<Detalle> detalles) {
		this.detalles = detalles;
	}
	public Long getTotal() {
		return total;
	}
	public void setTotal(Long total) {
		this.total = total;
	}

}
