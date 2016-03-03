package py.pol.una.ii.pw.model;

import java.util.ArrayList;

public class Compra {
	private Integer id;
	private Proveedor proveedor;
	private ArrayList<Detalle> detalles;
	private Long total;
	public Long getTotal() {
		return total;
	}
	public void setTotal(Long total) {
		this.total = total;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Proveedor getProveedor() {
		return proveedor;
	}
	public void setProveedor(Proveedor proveedor) {
		this.proveedor = proveedor;
	}
	public ArrayList<Detalle> getDetalles() {
		return detalles;
	}
	public void setDetalles(ArrayList<Detalle> detalles) {
		this.detalles = detalles;
	}
	

}
