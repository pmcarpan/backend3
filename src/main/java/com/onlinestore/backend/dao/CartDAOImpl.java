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
import com.onlinestore.backend.model.Product;
import com.onlinestore.backend.model.User;

@Repository
@Transactional
public class CartDAOImpl implements CartDAO {

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private CartDAO cDAO;
	@Autowired
	private ProductDAO pDAO;
	@Autowired
	private UserDAO uDAO;

	@Override
	public void saveOrUpdate(Cart c) {
		sessionFactory.getCurrentSession().saveOrUpdate(c);	
	}

	@Override
	public List<Cart> getAllCarts() {
		Session s = sessionFactory.getCurrentSession();
		TypedQuery<Cart> query = s.createQuery("from Cart", Cart.class);
        
        return query.getResultList();
	}

	@Override
	public Cart getCart(int id) {
		Session s = sessionFactory.getCurrentSession();
		TypedQuery<Cart> query = s.createQuery("from Cart where id = :id", Cart.class);
		query.setParameter("id", id);
        
        List<Cart> l = query.getResultList();
        
        if (l == null || l.size() == 0) {
        	return null;
        }
        
        return l.get(0);
	}

	@Override
	public void addProduct(String username, int productId) {
		System.out.println("\nCartDAO - addProduct");
		User u = uDAO.getUser(username);
		Product p = pDAO.getProduct(productId);
		Cart c = u.getCart();
		
		Set<Product> l = c.getProducts();

		if (!l.contains(p)) {
			l.add(p); 
			c.setNumItems(c.getNumItems() + 1);
			cDAO.saveOrUpdate(c);
		}

	}
	
	@Override
	public void removeProduct(String username, int productId) {
		User u = uDAO.getUser(username);
		Cart c = u.getCart();
		Set<Product> l = c.getProducts();
		Product p = pDAO.getProduct(productId);
		l.remove(p);
		c.setNumItems(c.getNumItems() - 1);
		
		saveOrUpdate(c);
	}
	
	@Override
	public void calcPrice(int id) {
		Cart c = getCart(id);
		double tot = 0;
		for (Product p : c.getProducts()) {
			System.out.println("Product " + p.getId() + "Price " + p.getPrice());
			tot += p.getPrice();
		}
		c.setTotPrice(tot);
		saveOrUpdate(c);
	}
	
	@Override
	public void delete(int id) {
		Cart c = new Cart();
		c.setId(id);
		sessionFactory.getCurrentSession().delete(c);
	}

}
