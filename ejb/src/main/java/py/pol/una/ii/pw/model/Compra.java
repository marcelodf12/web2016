package py.pol.una.ii.pw.model;

import java.io.Serializable;

import javax.persistence.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * The persistent class for the compras database table.
 * 
 */
@Entity
@Table(name = "COMPRAS")
@SequenceGenerator(name="seqCompra", initialValue=100, allocationSize=1, sequenceName="seqCompra")
public class Compra implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="seqCompra")
	@Column(name = "id_compra")
	private Long id;

	@Temporal(TemporalType.DATE)
	private Date fecha;

	@Column(name = "monto_total")
	private Integer montoTotal;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "id_compra_fk", referencedColumnName="id_compra")
	private List<CompraDetalle> compraDetalles;

	@ManyToOne
	@JoinColumn(name = "ruc_proveedor_fk")
	private Proveedor proveedor;

	public Compra() {
	}

	public void edit(String fecha, Integer montoTotal,
			List<CompraDetalle> compraDetalles, Proveedor proveedor) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		this.fecha = sdf.parse(fecha);
		this.montoTotal = montoTotal;
		this.compraDetalles = compraDetalles;
		this.proveedor = proveedor;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getFecha() {
		return this.fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public Integer getMontoTotal() {
		return this.montoTotal;
	}

	public void setMontoTotal(Integer montoTotal) {
		this.montoTotal = montoTotal;
	}

	public List<CompraDetalle> getCompraDetalles() {
		return this.compraDetalles;
	}

	public void setCompraDetalles(List<CompraDetalle> compraDetalles) {
		this.compraDetalles = compraDetalles;
	}

	public CompraDetalle addCompraDetalle(CompraDetalle compraDetalle) {
		getCompraDetalles().add(compraDetalle);
		compraDetalle.setCompra(this);

		return compraDetalle;
	}

	public CompraDetalle removeCompraDetalle(CompraDetalle compraDetalle) {
		getCompraDetalles().remove(compraDetalle);
		compraDetalle.setCompra(null);

		return compraDetalle;
	}

	public Proveedor getProveedor() {
		return this.proveedor;
	}

	public void setProveedor(Proveedor proveedore) {
		this.proveedor = proveedore;
	}

}
