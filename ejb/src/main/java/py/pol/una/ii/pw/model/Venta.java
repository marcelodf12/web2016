package py.pol.una.ii.pw.model;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import py.pol.una.ii.pw.dto.VentaDto;

/**
 * The persistent class for the ventas database table.
 * 
 */
@Entity
@Table(name = "VENTAS")
@SequenceGenerator(name="seqVenta", initialValue=100, allocationSize=1, sequenceName="seqVenta")
public class Venta implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="seqVenta")
	@Column(name = "id_venta")
	private Long id;

	@Temporal(TemporalType.DATE)
	private Date fecha;

	@Column(name = "monto_total")
	private Integer montoTotal;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "id_venta_fk", referencedColumnName="id_venta")
	private List<VentaDetalle> ventaDetalles;

	@ManyToOne
	@JoinColumn(name = "ruc_cliente_fk")
	private Cliente cliente;

	public Venta() {
	}
	
	public Venta(VentaDto ventaDto){
		this.fecha = ventaDto.getFecha();
		this.montoTotal = 0;
		this.ventaDetalles = new ArrayList<VentaDetalle>();
	}

	public void edit(String fecha, Integer montoTotal,
			List<VentaDetalle> ventaDetalles, Cliente cliente) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		this.fecha = sdf.parse(fecha);
		this.montoTotal = montoTotal;
		this.ventaDetalles = ventaDetalles;
		this.cliente = cliente;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getFecha() {
		return fecha==null? null : new Date(fecha.getTime());
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha==null? null : new Date(fecha.getTime());
	}

	public Integer getMontoTotal() {
		return this.montoTotal;
	}

	public void setMontoTotal(Integer montoTotal) {
		this.montoTotal = montoTotal;
	}

	public List<VentaDetalle> getVentaDetalles() {
		return this.ventaDetalles;
	}

	public void setVentaDetalles(List<VentaDetalle> ventaDetalles) {
		this.ventaDetalles = ventaDetalles;
	}

	public VentaDetalle addVentaDetalle(VentaDetalle ventaDetalle) {
		getVentaDetalles().add(ventaDetalle);
		ventaDetalle.setVenta(this);

		return ventaDetalle;
	}

	public VentaDetalle removeVentaDetalle(VentaDetalle ventaDetalle) {
		getVentaDetalles().remove(ventaDetalle);
		ventaDetalle.setVenta(null);

		return ventaDetalle;
	}

	public Cliente getCliente() {
		return this.cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

}
