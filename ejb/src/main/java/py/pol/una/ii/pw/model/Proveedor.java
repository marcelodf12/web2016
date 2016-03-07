package py.pol.una.ii.pw.model;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name="PROVEEDORES")
public class Proveedor implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ruc_proveedor")
	private String ruc;

	private Boolean activo;

	private String nombre;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "ruc_proveedor_fk", referencedColumnName="ruc_proveedor")
	@XmlTransient
	private Set<Compra> compras;

	public Proveedor() {
	}

	
	
	public Proveedor(String nombre, String ruc, String direccion) {
		super();
		this.ruc = ruc;
		this.nombre = nombre;
		this.activo = true;
	}



	public String getRuc() {
		return this.ruc;
	}

	public void setRuc(String ruc) {
		this.ruc = ruc;
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

	public Set<Compra> getCompras() {
		return this.compras;
	}

	public void setCompras(Set<Compra> compras) {
		this.compras = compras;
	}

	public Compra addCompra(Compra compra) {
		getCompras().add(compra);
		compra.setProveedor(this);

		return compra;
	}

	public Compra removeCompra(Compra compra) {
		getCompras().remove(compra);
		compra.setProveedor(null);

		return compra;
	}

}
