package com.onlinestore.backend.dao;

import java.security.GeneralSecurityException;
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
import com.onlinestore.backend.model.User;
import com.onlinestore.backend.utility.SHAUtil;

@Repository
@Transactional
public class UserDAOImpl implements UserDAO {

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private CartDAO cDAO;

	public void saveOrUpdate(User u) {
		try {
			if (getUser(u.getUsername()) == null)
				u.setPassword(SHAUtil.encode(u.getPassword()));
			
			u.setEnabled(true);
			
			if (u.getUsername().equals("arpan123")) u.setRole("ROLE_ADMIN");
			else u.setRole("ROLE_USER");
			
			Cart c = new Cart();
			u.setCart(c);
			cDAO.saveOrUpdate(c);
			sessionFactory.getCurrentSession().saveOrUpdate(u);	
		}
		catch (GeneralSecurityException gse) {
			System.out.println("UserDAOImpl saveOrUpdate() encoding error");
			gse.printStackTrace();
		}
		catch (Exception e) {
			System.out.println("UserDAOImpl saveOrUpdate()");
			e.printStackTrace();
		}
	}

	public List<User> getAllUsers() {
		Session s = sessionFactory.getCurrentSession();
		CriteriaBuilder builder = s.getCriteriaBuilder();
        CriteriaQuery<User> criteria = builder.createQuery(User.class);
        Root<User> root = criteria.from(User.class);
        criteria.select(root);
        
        Query<User> q = s.createQuery(criteria);
        
        return q.getResultList();
	}

	public User getUser(String username) {
		Session s = sessionFactory.getCurrentSession();
		CriteriaBuilder builder = s.getCriteriaBuilder();
        CriteriaQuery<User> criteria = builder.createQuery(User.class);
        Root<User> root = criteria.from(User.class);
        criteria.select(root);
        criteria.where( builder.equal( root.get("username"), username ) );
        
        Query<User> q = s.createQuery(criteria);
        
        if (q == null) {
        	return null;
        }
        
        List<User> l = q.getResultList();
        
        if (l == null || l.size() == 0) {
        	return null;
        }
        
        return l.get(0);
	}

	public void delete(String username) {
		User u = new User();
		u.setUsername(username);
		sessionFactory.getCurrentSession().delete(u);
	}

	public boolean validate(String username, String password) {
		User u = getUser(username);
		
		if (u == null) {
			return false;
		}
		
		return u.getPassword().equals(password);
	}
	
}
