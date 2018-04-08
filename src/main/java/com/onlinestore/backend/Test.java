package com.onlinestore.backend;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.onlinestore.backend.config.AppConfig;
import com.onlinestore.backend.dao.UserDAO;
import com.onlinestore.backend.model.User;

public class Test {

	public static void main(String[] args) {
		User u = new User();
		u.setUsername("arpan123");
		u.setPassword("password");
		u.setRole("ROLE_ADMIN");
		
		AnnotationConfigApplicationContext context = 
				new AnnotationConfigApplicationContext(AppConfig.class);
		
		UserDAO uDAO = context.getBean(UserDAO.class);
		uDAO.saveOrUpdate(u);
		
		context.close();
	}
	
}
