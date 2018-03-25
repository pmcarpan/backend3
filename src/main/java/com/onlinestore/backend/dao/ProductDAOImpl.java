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

import com.onlinestore.backend.model.Category;
import com.onlinestore.backend.model.Product;
import com.onlinestore.backend.model.Seller;

@Repository
@Transactional
public class ProductDAOImpl implements ProductDAO {

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private CategoryDAO cDAO;
	@Autowired
	private SellerDAO sDAO;
	
	public void saveOrUpdate(Product p) {
		Seller s = sDAO.getSeller(p.getSellerId());
		Category c = cDAO.getCategory(p.getCategoryId());
		
		p.setCategory(c);
		p.setSeller(s);
		
		List<Product> l;
		l = c.getProducts();
		if (l == null) l = new ArrayList<>();
		l.add(p);
		cDAO.saveOrUpdate(c);
		
		l = s.getProducts();
		if (l == null) l = new ArrayList<>();
		l.add(p);
		sDAO.saveOrUpdate(s);
		
		sessionFactory.getCurrentSession().saveOrUpdate(p);	
	}

	public List<Product> getAllProducts() {
		Session s = sessionFactory.getCurrentSession();
		CriteriaBuilder builder = s.getCriteriaBuilder();
        CriteriaQuery<Product> criteria = builder.createQuery(Product.class);
        Root<Product> root = criteria.from(Product.class);
        criteria.select(root);
        
        Query<Product> q = s.createQuery(criteria);
        
        return q.getResultList();
	}

	public List<Product> getAllProductsByCategory(int categoryId) {
		Session s = sessionFactory.getCurrentSession();
		CriteriaBuilder builder = s.getCriteriaBuilder();
        CriteriaQuery<Product> criteria = builder.createQuery(Product.class);
        Root<Product> root = criteria.from(Product.class);
        criteria.select(root);
        criteria.where( builder.equal( root.get("categoryId"), categoryId ) );
        
        Query<Product> q = s.createQuery(criteria);
        
        return q.getResultList();
	}

	public List<Product> getAllProductsBySeller(int sellerId) {
		Session s = sessionFactory.getCurrentSession();
		CriteriaBuilder builder = s.getCriteriaBuilder();
        CriteriaQuery<Product> criteria = builder.createQuery(Product.class);
        Root<Product> root = criteria.from(Product.class);
        criteria.select(root);
        criteria.where( builder.equal( root.get("sellerId"), sellerId ) );
        
        Query<Product> q = s.createQuery(criteria);
        
        return q.getResultList();
	}

	public Product getProduct(int id) {
		Session s = sessionFactory.getCurrentSession();
		CriteriaBuilder builder = s.getCriteriaBuilder();
        CriteriaQuery<Product> criteria = builder.createQuery(Product.class);
        Root<Product> root = criteria.from(Product.class);
        criteria.select(root);
        criteria.where( builder.equal( root.get("id"), id ) );
        
        Query<Product> q = s.createQuery(criteria);
        
        if (q == null) {
        	return null;
        }
        
        List<Product> l = q.getResultList();
        
        if (l == null || l.size() == 0) {
        	return null;
        }
        
        return l.get(0);
	}

	public void delete(int id) {
		Product p = getProduct(id);
		
		Seller s = sDAO.getSeller(p.getSellerId());
		s.getProducts().remove(p);
		
		Category c = cDAO.getCategory(p.getCategoryId());
		c.getProducts().remove(p);
		
		sessionFactory.getCurrentSession().delete(p);
	}

}
