package com.onlinestore.backend.dao;

import java.util.Set;

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
//	@Autowired
//	private CartDAO cDAO;

	@Override
	public void saveOrUpdate(OrderDetail oD, String username) {
		User u = uDAO.getUser(username);
		Cart c = u.getCart();
		oD.setCart(c);
		oD.setUser(u);
		oD.setStatus("Under Process");
		Set<OrderDetail> orders = u.getOrders();
		orders.add(oD);
		uDAO.saveOrUpdate(u);
		
		sessionFactory.getCurrentSession().saveOrUpdate(oD);
	}

	@Override
	public void delete(int id) {
		
	}
	
}
