package py.pol.una.ii.pw.dto;

import py.pol.una.ii.pw.model.Producto;

public class ProductoDto {
	private Long id;

	private Boolean activo;

	private String nombre;

	private Integer precio;

	private Integer stock;
	
	public ProductoDto(Producto p){
		id = p.getId();
		activo = p.getActivo();
		nombre = p.getNombre();
		precio = p.getPrecio();
		stock = p.getPrecio();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Boolean getActivo() {
		return activo;
	}

	public void setActivo(Boolean activo) {
		this.activo = activo;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Integer getPrecio() {
		return precio;
	}

	public void setPrecio(Integer precio) {
		this.precio = precio;
	}

	public Integer getStock() {
		return stock;
	}

	public void setStock(Integer stock) {
		this.stock = stock;
	}
	
	public String cad(){
		String r = "{" + nombre + "}\n";
		return r;
	}

}
