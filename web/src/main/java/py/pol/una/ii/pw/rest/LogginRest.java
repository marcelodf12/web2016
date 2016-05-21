package py.pol.una.ii.pw.rest;

import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import py.pol.una.ii.pw.dao.LoginDAO;
import py.pol.una.ii.pw.dto.UsuarioDto;
import py.pol.una.ii.pw.util.Respuesta;

@Path("/credenciales")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class LogginRest {
	
	@EJB
	private LoginDAO loginDao;
	

	@POST
	@Path("/login")
	public UsuarioDto login(UsuarioDto user){
		UsuarioDto respuesta = new UsuarioDto();
		respuesta.setPass("");
		respuesta.setNombre("");
		respuesta.setToken("");
		if(user.getNombre() != null && user.getPass() != null){
			String token = loginDao.login(user.getNombre(), user.getPass());
			if(token.compareTo("") != 0){
				respuesta.setNombre(user.getNombre());
				respuesta.setToken(token);
			}
		}
		return respuesta;
	}
	
	@POST
	@Path("/logout")
	public Respuesta<Boolean> logout(UsuarioDto user){
		Respuesta<Boolean> respuesta = new Respuesta<Boolean>();
		respuesta.setSuccess(false);
		if(user.getNombre()!= null){
			respuesta.setSuccess(loginDao.logout(user.getNombre()));
		}
		return respuesta;
	}
}
