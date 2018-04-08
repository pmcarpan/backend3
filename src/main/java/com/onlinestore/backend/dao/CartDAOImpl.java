package com.onlinestore.backend.dao;

import java.util.ArrayList;
import java.util.List;

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

@Repository
@Transactional
public class CartDAOImpl implements CartDAO {

	@Autowired
	private SessionFactory sessionFactory;

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

	public void addProduct(Cart c, Product p) {
		System.out.println("CartDAO - addProduct");
		List<Product> l = c.getProducts();
		if (l == null) {
			l = new ArrayList<>();
			c.setProducts(l);
		}
		if (!l.contains(p)) {
			l.add(p); // System.out.println("Added");
			c.setNumItems(c.getNumItems() + 1);
			c.setTotPrice(c.getTotPrice() + p.getPrice());
		}
		// System.out.println("cart " + c.getProducts());
	}
	
	public void delete(int id) {
		Cart c = new Cart();
		c.setId(id);
		sessionFactory.getCurrentSession().delete(c);
	}

}
