package com.onlinestore.backend;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.onlinestore.backend.config.AppConfig;
import com.onlinestore.backend.dao.ProductDAO;
import com.onlinestore.backend.model.Product;

public class Test {

	public static void main(String[] args) {
		Product p1 = new Product(); p1.setId(1); p1.setName("iPhone"); p1.setPrice(10000.34);
		Product p2 = new Product(); p2.setId(3); p2.setName("iPad"); p2.setPrice(99000.34);
		
		AnnotationConfigApplicationContext context = 
				new AnnotationConfigApplicationContext(AppConfig.class);
		
		ProductDAO pDAO = context.getBean(ProductDAO.class);
		pDAO.saveOrUpdate(p1); 
		pDAO.saveOrUpdate(p2);
		for (Product p : pDAO.getAllProducts()) 
			System.out.println(p.getName());
		pDAO.delete(1);
		
		context.close();
	}
	
}
