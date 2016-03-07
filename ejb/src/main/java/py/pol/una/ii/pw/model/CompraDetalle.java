package py.pol.una.ii.pw.model;

import java.io.Serializable;

import javax.persistence.*;

@Entity
@Table(name="COMPRA_DETALLES")
@SequenceGenerator(name="seqCompraDetalle", initialValue=100, allocationSize=1, sequenceName="seqCompraDetalle")
public class CompraDetalle implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="seqCompraDetalle")
	@Column(name="compra_detalle_id")
	private Integer id;

	private Integer cantidad;

	private Integer precio;

	@ManyToOne
	@JoinColumn(name="id_compra_fk")
	private Compra compra;

	@ManyToOne
	@JoinColumn(name="id_producto_fk_compra")
	private Producto producto;

	public CompraDetalle() {
	}

	
	
	public CompraDetalle(Integer cantidad, Integer precio, Producto producto) {
		super();
		this.cantidad = cantidad;
		this.precio = precio;
		this.producto = producto;
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

	public Compra getCompra() {
		return this.compra;
	}

	public void setCompra(Compra compra) {
		this.compra = compra;
	}

	public Producto getProducto() {
		return this.producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}

}
