package py.pol.una.ii.pw.dao;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Random;

import javax.ejb.EJB;
import javax.ejb.Stateful;

import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.managed.ManagedTransactionFactory;

import py.pol.una.ii.pw.mapper.LoginMapper;
import py.pol.una.ii.pw.service.MyBatisSingleton;

@Stateful
public class LoginDAO {
	
	@EJB
	private MyBatisSingleton mb;
	
	private SqlSession session;
	
	private LoginMapper cm;
	
	public void init(){
		TransactionFactory transactionFactory = new ManagedTransactionFactory();
		Environment environment = new Environment("development", transactionFactory, mb.getSource());
		Configuration configuration = new Configuration(environment);
		configuration.addMapper(LoginMapper.class);
		SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);
		session = sqlSessionFactory.openSession();
		try {
			cm = session.getMapper(LoginMapper.class);
		} catch (Exception e) {
			System.out.println("Error al abrir la base de datos");
			System.out.println(e.getMessage());
		}
	}
	
	public String login(String nombre, String pass){
		init();
		try {
			String passMd5 = cm.getPass(nombre);
			if(passMd5 == null)
				return "";
			String passMd5Test = encriptaEnMD5(pass);
			//System.out.println("Autenticación");
			//System.out.print(passMd5 + "=" + passMd5Test);
			if(passMd5.compareTo(passMd5Test)==0){
				String ultimoToken = cm.getToken(nombre);
				if(ultimoToken.compareTo("#")==0){
					Random rnd = new Random();
					Date d = new Date();
					String t = nombre + d.toString() +  Double.toString(rnd.nextDouble());
					String token = encriptaEnMD5(t);
					cm.login(token, nombre);
					
					return token;
				}else{
					return ultimoToken;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			session.rollback();
			return "";
		} finally {
			session.close();
		}
		return "";
	}
	
	public boolean logout(String nombre){
		init();
		boolean r = false;
		try{
			cm.logout(nombre);
			r= true;
		}catch (Exception e){
			e.printStackTrace();
		} finally {
			session.close();
		}
		return r;
		
	}
	
	public String getRol(String token){
		init();
		String rol = cm.getRol(token);
		session.close();
		return rol;
	}
	
	 private char[] CONSTS_HEX = { '0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f' };
	 
	 private String encriptaEnMD5(String stringAEncriptar)
	    {
	        try
	        {
	           MessageDigest msgd = MessageDigest.getInstance("MD5");
	           byte[] bytes = msgd.digest(stringAEncriptar.getBytes(Charset.forName("UTF-8")));
	           StringBuilder strbCadenaMD5 = new StringBuilder(2 * bytes.length);
	           for (int i = 0; i < bytes.length; i++)
	           {
	               int bajo = (int)(bytes[i] & 0x0f);
	               int alto = (int)((bytes[i] & 0xf0) >> 4);
	               strbCadenaMD5.append(CONSTS_HEX[alto]);
	               strbCadenaMD5.append(CONSTS_HEX[bajo]);
	           }
	           return strbCadenaMD5.toString();
	        } catch (NoSuchAlgorithmException e) {
	           return null;
	        }
	    }
}
