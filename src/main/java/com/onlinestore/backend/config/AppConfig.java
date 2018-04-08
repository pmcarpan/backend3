package com.onlinestore.backend.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBuilder;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.onlinestore.backend.model.Cart;
import com.onlinestore.backend.model.Category;
import com.onlinestore.backend.model.OrderDetail;
import com.onlinestore.backend.model.Product;
import com.onlinestore.backend.model.Seller;
import com.onlinestore.backend.model.User;

@Configuration
@EnableTransactionManagement
@ComponentScan("com.onlinestore.backend")
public class AppConfig {

	@Bean(name = "dataSource")
	public DataSource getDataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();

		dataSource.setUrl("jdbc:h2:tcp://localhost/~/arpan");
		dataSource.setDriverClassName("org.h2.Driver");
		dataSource.setUsername("sa");
		dataSource.setPassword("sa");

		return dataSource;
	}

	@Autowired
	@Bean(name = "sessionFactory")
	public SessionFactory getSessionFactory(DataSource dataSource) {	
		LocalSessionFactoryBuilder factoryBean = new LocalSessionFactoryBuilder(dataSource);

		Properties props = new Properties();
		props.put("hibernate.hbm2ddl.auto", "update");
		props.put("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
		props.put("hibernate.show_sql", true);
		
		factoryBean.addProperties(props);
		
		factoryBean.addAnnotatedClasses(User.class, Cart.class, Seller.class, 
										Category.class, Product.class, OrderDetail.class);
		
		return factoryBean.buildSessionFactory();
	}

	@Autowired
	@Bean(name="transactionManager")
	public HibernateTransactionManager getTransactionManager(SessionFactory sessionFactory) {
		HibernateTransactionManager transactionManager = new HibernateTransactionManager(sessionFactory);	
		
		return transactionManager;
	}
}
