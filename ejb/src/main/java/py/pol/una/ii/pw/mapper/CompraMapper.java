package py.pol.una.ii.pw.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import py.pol.una.ii.pw.model.Compra;

public interface CompraMapper {
	
	@Select("SELECT id_compra as id, fecha, monto_total as montoTotal FROM COMPRAS LIMIT #{limit} OFFSET #{offset};")
	List<Compra> getAllCompras(@Param("limit") Integer limit, @Param("offset") Integer offset); 
	
	@Select("SELECT id_compra as id, fecha, monto_total as montoTotal FROM COMPRAS WHERE id=#{idCompra}")
	Compra getCompra(@Param("idCompra") Long id);
	
	@Insert("INSERT INTO compras(id_compra, fecha, monto_total, ruc_proveedor_fk) VALUES (#{id_compra}, #{fecha}, #{monto_total}, #{ruc_proveedor_fk});")
	void persistCompra(
			@Param("id_compra") Long id_compra,
			@Param("fecha") Date fecha,
			@Param("monto_total") Integer monto_total,
			@Param("ruc_proveedor_fk") String ruc_proveedor_fk);
	
	@Insert("INSERT INTO compra_detalles(compra_detalle_id, cantidad, precio, id_compra_fk, id_producto_fk_compra) VALUES (#{compra_detalle_id}, #{cantidad}, #{precio}, #{id_compra_fk}, #{id_producto_fk_compra});")
	void nuevoDetalle(
			@Param("compra_detalle_id") Long compra_detalle_id,
			@Param("cantidad") Integer cantidad,
			@Param("precio") Integer precio,
			@Param("id_compra_fk") Long id_compra_fk,
			@Param("id_producto_fk_compra") Long id_producto_fk_compra
			);
	@Select("SELECT nextVal('seqcompra')")
	Long generarIdCompra();
	
	@Select("SELECT nextVal('seqcompradetalle')")
	Long generarIdCompraDetalle();
}
