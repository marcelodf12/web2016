package py.pol.una.ii.pw.service;

import javax.ejb.Stateless;

import py.pol.una.ii.pw.model.Producto;

@Stateless
public class ProductoEjb {
	
	public Producto nuevo(Producto p){
		return p;
	}
}
