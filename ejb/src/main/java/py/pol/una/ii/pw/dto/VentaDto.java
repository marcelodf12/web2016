package py.pol.una.ii.pw.dto;

import java.util.Date;
import java.util.List;

public class VentaDto {
	private Long id;

	private Date fecha;

	private Integer montoTotal;

	private List<VentaDetalleDto> ventaDetalles;

	private String rucCliente;

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

	public List<VentaDetalleDto> getVentaDetalles() {
		return ventaDetalles;
	}

	public void setVentaDetalles(List<VentaDetalleDto> ventaDetalles) {
		this.ventaDetalles = ventaDetalles;
	}

	public String getRucCliente() {
		return rucCliente;
	}

	public void setRucCliente(String rucCliente) {
		this.rucCliente = rucCliente;
	}
	
	

}
