package py.pol.una.ii.pw.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import py.pol.una.ii.pw.model.Producto;

public interface ProductoMapper {
	
	@Insert("INSERT INTO productos(id_producto, activo, nombre, precio, stock) VALUES (#{id_producto}, #{activo}, #{nombre}, #{precio}, #{stock});")
	void persistirProducto(
			@Param("id_producto") Long id_producto,
			@Param("activo") Boolean activo,
			@Param("nombre") String nombre,
			@Param("precio") Integer precio,
			@Param("stock") Integer stock
			);
	
	@Select("SELECT id_producto as id, activo, nombre, precio, stock FROM productos WHERE id_producto=#{id_producto} AND activo=true;")
	Producto getProducto(@Param("id_producto") Long id_producto);
	
	@Update("UPDATE productos SET activo=#{activo}, nombre=#{nombre}, precio=#{precio}, stock=#{stock} WHERE id_producto=#{id_producto};")
	void udateProducto(
			@Param("id_producto") Long id_producto,
			@Param("activo") Boolean activo,
			@Param("nombre") String nombre,
			@Param("precio") Integer precio,
			@Param("stock") Integer stock
			);
	@Update("UPDATE productos SET stock=#{stock} WHERE id_producto=#{id_producto};")
	void udateProductoStock(
			@Param("id_producto") Long id_producto,
			@Param("stock") Integer stock
			);
}
