package py.pol.una.ii.pw.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import py.pol.una.ii.pw.model.Venta;

public interface VentaMapper {
	
	@Select("SELECT id_venta as id, fecha, monto_total as montoTotal FROM COMPRAS LIMIT #{limit} OFFSET #{offset};")
	List<Venta> getAllVentas(@Param("limit") Integer limit, @Param("offset") Integer offset); 
	
	@Select("SELECT id_venta as id, fecha, monto_total as montoTotal FROM COMPRAS WHERE id=#{idVenta}")
	Venta getVenta(@Param("idVenta") Long id);
	
	@Insert("INSERT INTO ventas(id_venta, fecha, monto_total, ruc_cliente_fk) VALUES (#{id_venta}, #{fecha}, #{monto_total}, #{ruc_cliente_fk});")
	void persistVenta(
			@Param("id_venta") Long id_venta,
			@Param("fecha") Date fecha,
			@Param("monto_total") Integer monto_total,
			@Param("ruc_cliente_fk") String ruc_cliente_fk);
	
	@Insert("INSERT INTO venta_detalle(id, cantidad, precio, id_venta_fk, id_producto_fk_venta) VALUES (#{venta_detalle_id}, #{cantidad}, #{precio}, #{id_venta_fk}, #{id_producto_fk_venta});")
	void nuevoDetalle(
			@Param("venta_detalle_id") Long venta_detalle_id,
			@Param("cantidad") Integer cantidad,
			@Param("precio") Integer precio,
			@Param("id_venta_fk") Long id_venta_fk,
			@Param("id_producto_fk_venta") Long id_producto_fk_venta
			);
	@Select("SELECT nextVal('seqventa')")
	Long generarIdVenta();
	
	@Select("SELECT nextVal('seqventadetalle')")
	Long generarIdVentaDetalle();
}
