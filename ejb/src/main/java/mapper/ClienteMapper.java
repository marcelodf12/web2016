package mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import py.pol.una.ii.pw.model.Cliente;

public interface ClienteMapper {
	
	@Select("SELECT ruc_cliente as ruc, activo, deuda, nombre FROM clientes WHERE activo=true;")
	List<Cliente> getAllClientes(); 
	
	@Select("SELECT ruc_cliente as ruc, activo, deuda, nombre FROM clientes WHERE ruc_cliente=#{ruc} AND activo=true;")
	Cliente getCliente(String ruc);
	
	@Update("UPDATE CLIENTES SET deuda=#{d}, activo=#{a}, nombre=#{n} WHERE ruc_cliente=#{r}")
	void updateCliente(@Param("r") String r, @Param("d") Integer d, @Param("a") Boolean a, @Param("n") String n);
	
	@Insert("INSERT INTO CLIENTES(deuda, activo, nombre, ruc_cliente) VALUES(#{d}, true, #{n}, #{r})")
	void persistCliente(@Param("r") String r, @Param("d") Integer d, @Param("n") String n);
}
