package com.onlinestore.backend.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.onlinestore.backend.model.Cart;
import com.onlinestore.backend.model.OrderDetail;
import com.onlinestore.backend.model.User;

@Repository
@Transactional
public class OrderDetailDAOImpl implements OrderDetailDAO {

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private UserDAO uDAO;
	@Autowired
	private CartDAO cDAO;

	@Override
	public void saveOrUpdate(OrderDetail oD, String username, Cart c) {
		cDAO.saveOrUpdate(c);
		User u = uDAO.getUser(username);
		oD.setCart(c);
		oD.setUser(u);
		oD.setStatus("Under Process");
		List<OrderDetail> orders = u.getOrders();
		if (orders == null) {
			orders = new ArrayList<>();
			u.setOrders(orders);
		}
		orders.add(oD);
		uDAO.saveOrUpdate(u);
		
		sessionFactory.getCurrentSession().saveOrUpdate(oD);
	}

	@Override
	public void delete(int id) {
		
	}
	
}
