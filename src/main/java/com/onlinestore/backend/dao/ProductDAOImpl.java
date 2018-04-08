package com.onlinestore.backend.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.HibernateException;
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
		Product p1;
		Seller s;
		Category c;
		
		p1 = getProduct(p.getId());
		
		if (p1 != null) {
			s = p1.getSeller();
			if (s != null) {s.getProducts().remove(p1); sDAO.saveOrUpdate(s);}
			c = p1.getCategory();
			if (c != null) {c.getProducts().remove(p1); cDAO.saveOrUpdate(c);}
		}
		
		p1.setId(p.getId());
		p1.setName(p.getName());
		p1.setDescription(p.getDescription());
		p1.setPrice(p.getPrice());
		p1.setCategoryId(p.getCategoryId());
		p1.setSellerId(p.getSellerId());
		
		s = sDAO.getSeller(p1.getSellerId());
		c = cDAO.getCategory(p1.getCategoryId());
		p1.setSeller(s);
		p1.setCategory(c);
		
		List<Product> l;
		l = c.getProducts();
		if (l == null) l = new ArrayList<>();
		l.add(p1);
		cDAO.saveOrUpdate(c);
		
		l = s.getProducts();
		if (l == null) l = new ArrayList<>();
		l.add(p1);
		sDAO.saveOrUpdate(s);
		
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(p1);
		} 
		catch (HibernateException e) {
			System.out.println("ERROR: PRODUCTDAOIMPL");
			e.printStackTrace();
		}	
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
		System.out.println("---");
		Product p = getProduct(id);
		
		Category c = p.getCategory();
		c.getProducts().remove(p); cDAO.saveOrUpdate(c);
		System.out.println(c.getProducts());
		Seller s = p.getSeller();
		s.getProducts().remove(p); sDAO.saveOrUpdate(s);
		System.out.println(s.getProducts());
//
//		Category c = cDAO.getCategory(p.getCategoryId());
//		c.getProducts().remove(p);

		sessionFactory.getCurrentSession().delete(p);
	}

}
