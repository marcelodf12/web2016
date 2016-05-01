package py.pol.una.ii.pw.service;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;

import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.postgresql.ds.PGPoolingDataSource;

import py.pol.una.ii.pw.mapper.ClienteMapper;
import py.pol.una.ii.pw.mapper.CompraMapper;
import py.pol.una.ii.pw.mapper.ProductoMapper;
import py.pol.una.ii.pw.mapper.ProveedorMapper;
import py.pol.una.ii.pw.mapper.VentaMapper;


@Singleton
public class MyBatisSingleton {
	
	private SqlSessionFactory sqlSessionFactory;

	@PostConstruct
	private void init(){
		PGPoolingDataSource source = new PGPoolingDataSource();
		source.setDataSourceName("A Data Source");
		source.setServerName("localhost");
		source.setDatabaseName("web");
		source.setUser("postgres");
		source.setPassword("postgres");
		source.setMaxConnections(10);
		TransactionFactory transactionFactory = new JdbcTransactionFactory();
		Environment environment = new Environment("development", transactionFactory, source);
		Configuration configuration = new Configuration(environment);
		configuration.addMapper(ClienteMapper.class);
		configuration.addMapper(ProveedorMapper.class);
		configuration.addMapper(CompraMapper.class);
		configuration.addMapper(ProductoMapper.class);
		configuration.addMapper(VentaMapper.class);
		sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);

		System.out.println("Inicialización");
	}
	
	public SqlSession getSession(){
		return sqlSessionFactory.openSession();
	}
}
