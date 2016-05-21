package py.pol.una.ii.py.interceptor;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.servlet.http.HttpServletRequest;

import py.pol.una.ii.pw.dao.LoginDAO;
import py.pol.una.ii.pw.util.Respuesta;
import py.pol.una.ii.py.annotation.Secured;

@Secured
@Interceptor
public class SecureInterceptor {

	@Inject
	private LoginDAO loginDao;
	
	@Inject
    HttpServletRequest request;

	/**
	 * @param ic
	 * @return
	 * @throws Exception
	 */
	@AroundInvoke
	public Object aroundInvoke(InvocationContext ic) throws Exception {
		String token = request.getHeader("token");
		if(token!=null){
			if(token.compareTo("#")!=0){
				String rol = loginDao.getRol(token);
				String rolNeed = ic.getMethod().getAnnotation(Secured.class).rol();
				if(rol!=null){
					if(rolNeed.compareTo(rol)==0){
						return ic.proceed();
					}
				}
			}
		}
		Respuesta<Object> r = new Respuesta<Object>();
		r.setSuccess(false);
		r.setMessages("Error de autenticacion");
		r.setReason("Token no valido");
		return r;
		
	}

}
