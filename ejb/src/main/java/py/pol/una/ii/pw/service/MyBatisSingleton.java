package py.pol.una.ii.pw.service;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;

import org.postgresql.ds.PGPoolingDataSource;


@Singleton
public class MyBatisSingleton {
	
	private PGPoolingDataSource source;

	@PostConstruct
	private void init(){
		source = new PGPoolingDataSource();
		source.setDataSourceName("A Data Source");
		source.setServerName("localhost");
		source.setDatabaseName("web");
		source.setUser("postgres");
		source.setPassword("postgres");
		source.setMaxConnections(1000);
		System.out.println("Inicialización");
	}
	
	public PGPoolingDataSource getSource(){
		return source;
	}
}
