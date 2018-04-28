package com.onlinestore.backend.dao;

import java.util.List;

import javax.persistence.TypedQuery;
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
import com.onlinestore.backend.model.Seller;

@Repository
@Transactional
public class CategoryDAOImpl implements CategoryDAO {

	@Autowired
	private SessionFactory sessionFactory;
	
	public void saveOrUpdate(Category c) {
		c.setEnabled(true);
		sessionFactory.getCurrentSession().saveOrUpdate(c);	
	}

	public List<Category> getAllCategories() {
		Session s = sessionFactory.getCurrentSession();
		TypedQuery<Category> query = s.createQuery("from Category where enabled = true", Category.class);
        
        return query.getResultList();
	}

	public Category getCategory(int id) {
		Session s = sessionFactory.getCurrentSession();
		TypedQuery<Category> query = s.createQuery("from Category where id = :id", Category.class);
        query.setParameter("id", id);
		
        List<Category> l = query.getResultList();
        
        if (l == null || l.size() == 0) {
        	return null;
        }
        
        return l.get(0);
	}

	public void delete(int id) {
		Session sess = sessionFactory.getCurrentSession();
		Category c = getCategory(id);
		c.setEnabled(false);
		sess.saveOrUpdate(c);
	}

}
