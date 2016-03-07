package py.pol.una.ii.pw.model;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.xml.bind.annotation.XmlTransient;


/**
 * The persistent class for the productos database table.
 * 
 */
@Entity
@Table(name="PRODUCTOS")
@SequenceGenerator(name="seqProducto", initialValue=100, allocationSize=1, sequenceName="seqProducto")
public class Producto implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="seqProducto")
	@Column(name="id_producto")
	private Long id;

	private Boolean activo;

	private String nombre;

	private Integer precio;

	@Min(0)
	private Integer stock;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "id_producto_fk_compra", referencedColumnName="id_producto")
	@XmlTransient
	private Set<Compra> compras;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "id_producto_fk_venta", referencedColumnName="id_producto")
	@XmlTransient
	private Set<Venta> ventas;

	public Producto() {
	}

	
	public Producto(String nombre, Integer precio) {
		super();
		this.nombre = nombre;
		this.precio = precio;
		this.stock = 0;
		this.activo = true;
	}


	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Boolean getActivo() {
		return this.activo;
	}

	public void setActivo(Boolean activo) {
		this.activo = activo;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Integer getPrecio() {
		return this.precio;
	}

	public void setPrecio(Integer precio) {
		this.precio = precio;
	}

	public Integer getStock() {
		return this.stock;
	}

	public void setStock(Integer stock) {
		this.stock = stock;
	}

}
