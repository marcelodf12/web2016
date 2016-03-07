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
@Table(name="CLIENTES")
public class Cliente implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ruc_cliente")
	private String ruc;

	private Boolean activo;

	private String nombre;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "ruc_cliente_fk", referencedColumnName="ruc_cliente")
	@XmlTransient
	private Set<Venta> ventas;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "fk_ruc_cliente_pago", referencedColumnName="ruc_cliente")
	@XmlTransient
	private Set<Pago> pagos;

	public Cliente() {
	}

	
	
	public Set<Pago> getPagos() {
		return pagos;
	}



	public void setPagos(Set<Pago> pagos) {
		this.pagos = pagos;
	}



	public Cliente(String nombre, String ruc, String direccion) {
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

	public Set<Venta> getVentas() {
		return this.ventas;
	}

	public void setVentas(Set<Venta> ventas) {
		this.ventas = ventas;
	}

	public Venta addVenta(Venta venta) {
		getVentas().add(venta);
		venta.setCliente(this);

		return venta;
	}

	public Venta removeVenta(Venta venta) {
		getVentas().remove(venta);
		venta.setCliente(null);

		return venta;
	}

}
