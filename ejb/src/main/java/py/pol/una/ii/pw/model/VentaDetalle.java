package py.pol.una.ii.pw.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name="VENTA_DETALLE")
@SequenceGenerator(name="seqVentaDetalle", initialValue=100, allocationSize=1, sequenceName="seqVentaDetalle")
public class VentaDetalle implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="seqVentaDetalle")
	private Integer id;

	@Min(0)
	private Integer cantidad;

	private Integer precio;

	@ManyToOne
	@JoinColumn(name="id_producto_fk_venta")
	private Producto producto;
	
	@ManyToOne
	@JoinColumn(name="id_venta_fk")
	@XmlTransient
	private Venta venta;

	public VentaDetalle() {
	}

	public VentaDetalle(Integer cantidad, Integer precio, Producto p) {
		super();
		this.cantidad = cantidad;
		this.precio = precio;
		this.setProducto(p);
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getCantidad() {
		return this.cantidad;
	}

	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}

	public Integer getPrecio() {
		return this.precio;
	}

	public void setPrecio(Integer precio) {
		this.precio = precio;
	}

	public Producto getProducto() {
		return this.producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}

	@XmlTransient
	public Venta getVenta() {
		return venta;
	}

	public void setVenta(Venta venta) {
		this.venta = venta;
	}

}
