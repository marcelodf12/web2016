package py.pol.una.ii.pw.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import py.pol.una.ii.pw.model.Proveedor;

public interface ProveedorMapper {
	
	@Select("SELECT ruc_proveedor as ruc, activo, nombre FROM PROVEEDORES WHERE activo=true;")
	List<Proveedor> getAllProveedores(); 
	
	@Select("SELECT ruc_proveedor as ruc, activo, nombre FROM proveedores WHERE ruc_proveedor=#{ruc} AND activo=true;")
	Proveedor getProveedor(String ruc);
	
	@Update("UPDATE PROVEEDORES SET activo=#{a}, nombre=#{n} WHERE ruc_proveedor=#{r}")
	void updateProveedor(@Param("r") String r, @Param("a") Boolean a, @Param("n") String n);
	
	@Insert("INSERT INTO PROVEEDORES(activo, nombre, ruc_proveedor) VALUES(true, #{n}, #{r})")
	void persistProveedor(@Param("r") String r, @Param("n") String n);
}
