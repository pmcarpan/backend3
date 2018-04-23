package com.onlinestore.backend.dao;

import java.util.List;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
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

	public void saveOrUpdate(Cart c) {
		sessionFactory.getCurrentSession().saveOrUpdate(c);	
	}

	public List<Cart> getAllCarts() {
		Session s = sessionFactory.getCurrentSession();
		CriteriaBuilder builder = s.getCriteriaBuilder();
        CriteriaQuery<Cart> criteria = builder.createQuery(Cart.class);
        Root<Cart> root = criteria.from(Cart.class);
        criteria.select(root);
        
        Query<Cart> q = s.createQuery(criteria);
        
        return q.getResultList();
	}

	public List<Cart> getAllCarts(String username) {
		Session s = sessionFactory.getCurrentSession();
		CriteriaBuilder builder = s.getCriteriaBuilder();
        CriteriaQuery<Cart> criteria = builder.createQuery(Cart.class);
        Root<Cart> root = criteria.from(Cart.class);
        criteria.select(root);
        criteria.where( builder.equal( root.get("username"), username ) );
        
        Query<Cart> q = s.createQuery(criteria);
        
        return q.getResultList();
	}

	public Cart getCart(int id) {
		Session s = sessionFactory.getCurrentSession();
		CriteriaBuilder builder = s.getCriteriaBuilder();
        CriteriaQuery<Cart> criteria = builder.createQuery(Cart.class);
        Root<Cart> root = criteria.from(Cart.class);
        criteria.select(root);
        criteria.where( builder.equal( root.get("id"), id ) );
        
        Query<Cart> q = s.createQuery(criteria);
        
        if (q == null) {
        	return null;
        }
        
        List<Cart> l = q.getResultList();
        
        if (l == null || l.size() == 0) {
        	return null;
        }
        
        return l.get(0);
	}

	public void addProduct(String username, int productId) {
		System.out.println("\nCartDAO - addProduct");
		User u = uDAO.getUser(username);
		Product p = pDAO.getProduct(productId);
		Cart c = u.getCart();
		
		Set<Product> l = c.getProducts();
		
		System.out.println("Initial: " + l);

		if (!l.contains(p)) {
			l.add(p); 
			c.setNumItems(c.getNumItems() + 1);
			cDAO.saveOrUpdate(c);
		}
		
		System.out.println("Final: " + l);

	}
	
	public void removeProduct(String username, int productId) {
		User u = uDAO.getUser(username);
		Cart c = u.getCart();
		Set<Product> l = c.getProducts();
		Product p = pDAO.getProduct(productId);
		l.remove(p);
		c.setNumItems(c.getNumItems() - 1);
		
		saveOrUpdate(c);
	}
	
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
	
	public void delete(int id) {
		Cart c = new Cart();
		c.setId(id);
		sessionFactory.getCurrentSession().delete(c);
	}

}
