package py.pol.una.ii.pw.model;

import java.util.ArrayList;

public class Cliente {
	
	private Integer id;
	
	private String nombre;
	
	private Long deuda;
	
	private String cedula;
	
	private ArrayList<Pago> pagos;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Long getDeuda() {
		return deuda;
	}

	public void setDeuda(Long deuda) {
		this.deuda = deuda;
	}

	public String getCedula() {
		return cedula;
	}

	public void setCedula(String cedula) {
		this.cedula = cedula;
	}

	public ArrayList<Pago> getPagos() {
		return pagos;
	}

	public void setPagos(ArrayList<Pago> pagos) {
		this.pagos = pagos;
	}
	
	

}
