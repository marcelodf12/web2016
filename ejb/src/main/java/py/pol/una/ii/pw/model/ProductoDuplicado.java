package py.pol.una.ii.pw.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="PRODUCTOS_DUPLICADOS")
@SequenceGenerator(name="seqProductoR", initialValue=100, allocationSize=1, sequenceName="seqProductoR")
public class ProductoDuplicado {
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="seqProductoR")
	@Column(name="id_producto_duplicado")
	private Long id;
	
	@OneToOne
	@JoinColumn(name="id_producto")
	private Producto producto;

	private Integer cantidad;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Producto getProducto() {
		return producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}

	public Integer getCantidad() {
		return cantidad;
	}

	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}
	
	

}
