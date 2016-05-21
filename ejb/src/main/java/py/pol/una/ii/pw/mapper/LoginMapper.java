package py.pol.una.ii.pw.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface LoginMapper {
	
	@Update("UPDATE USUARIOS SET token=#{t} WHERE nombre=#{n}")
	void login(@Param("t") String t, @Param("n") String n);
	
	@Update("UPDATE USUARIOS SET token='#' WHERE nombre=#{n}")
	void logout(@Param("n") String n);
	
	@Select("SELECT token FROM USUARIOS WHERE nombre=#{n};")
	String getToken(@Param("n") String n);
	
	@Select("SELECT rol FROM USUARIOS WHERE nombre=#{n};")
	String getRol(@Param("n") String n);
	
	@Select("SELECT pass FROM USUARIOS WHERE nombre=#{n};")
	String getPass(@Param("n") String n);


}
