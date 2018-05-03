package com.onlinestore.backend.dao;

import java.util.List;
import java.util.Set;

import javax.persistence.TypedQuery;

import org.hibernate.Session;
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
	public List<OrderDetail> getAllOrders() {
		Session s = sessionFactory.getCurrentSession();
		TypedQuery<OrderDetail> query = s.createQuery("from OrderDetail", OrderDetail.class);
		
		return query.getResultList();
	}
	
	@Override
	public List<OrderDetail> getAllOrders(String username) {
		Session s = sessionFactory.getCurrentSession();
		TypedQuery<OrderDetail> query = s.createQuery("from OrderDetail where user = :user", OrderDetail.class);
		query.setParameter("user", uDAO.getUser(username));
		
		return query.getResultList();
	}
	
	@Override
	public OrderDetail getOrderById(int id) {
		Session s = sessionFactory.getCurrentSession();
		TypedQuery<OrderDetail> query = s.createQuery("from OrderDetail where id = :id", OrderDetail.class);
		query.setParameter("id", id);
		
		List<OrderDetail> l = query.getResultList();
		
		if (l == null || l.isEmpty()) {
			return null;
		}
		
		return l.get(0);
	}
	
	@Override
	public void delete(int id) {
		OrderDetail oD = new OrderDetail();
		oD.setId(id);
		sessionFactory.getCurrentSession().delete(oD);
	}
	
}
