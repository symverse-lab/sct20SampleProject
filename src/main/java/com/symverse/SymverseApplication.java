package com.symverse;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
 
@SpringBootApplication
@ComponentScan
public class SymverseApplication {	

	
	
	
	public static void main(String[] args) {		
		
		SpringApplication.run(SymverseApplication.class, args);
	}

	   
	/*
	 * @Bean public SqlSessionFactory sqlSessionFactory(DataSource dataSource)
	 * throws Exception{ SqlSessionFactoryBean sessionFactory = new
	 * SqlSessionFactoryBean(); sessionFactory.setDataSource(dataSource);
	 * 
	 * Resource[] res = new PathMatchingResourcePatternResolver().getResources(
	 * "classpath:mappers/*Mapper.xml"); sessionFactory.setMapperLocations(res);
	 * 
	 * return sessionFactory.getObject(); }
	 */
 
	
}
