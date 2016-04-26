package mapper;

import java.util.List;

import org.apache.ibatis.annotations.Select;
import py.pol.una.ii.pw.model.Cliente;

public interface ClienteMapper {
	
	@Select("SELECT ruc_cliente, activo, deuda, nombre FROM clientes;")
	List<Cliente> getAllClientes(); 
}
