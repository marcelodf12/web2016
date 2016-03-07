package py.pol.una.ii.pw.dto;

import java.util.Date;
import java.util.List;

public class CompraDto {
	private Long id;

	private Date fecha;

	private Integer montoTotal;

	private List<CompraDetalleDto> compraDetalles;

	private String rucProveedor;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public Integer getMontoTotal() {
		return montoTotal;
	}

	public void setMontoTotal(Integer montoTotal) {
		this.montoTotal = montoTotal;
	}

	public List<CompraDetalleDto> getCompraDetalles() {
		return compraDetalles;
	}

	public void setCompraDetalles(List<CompraDetalleDto> compraDetalles) {
		this.compraDetalles = compraDetalles;
	}

	public String getRucProveedor() {
		return rucProveedor;
	}

	public void setRucProveedor(String rucProveedor) {
		this.rucProveedor = rucProveedor;
	}
	
	

}
