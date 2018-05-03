package com.onlinestore.backend.dao;

import java.security.GeneralSecurityException;
import java.util.List;

import javax.persistence.TypedQuery;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
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

	@Override
	public void saveOrUpdate(User u) {
		try {
			// encode password for new user
			if (getUser(u.getUsername()) == null) {
				u.setPassword(SHAUtil.encode(u.getPassword()));
			}
			
			u.setEnabled(true);
			
			if (u.getUsername().equals("arpan123")) u.setRole("ROLE_ADMIN");
			else u.setRole("ROLE_USER");
			
			Cart c = new Cart();
			u.setCart(c);
			
			cDAO.saveOrUpdate(c);
			sessionFactory.getCurrentSession().saveOrUpdate(u);	
		}
		catch (GeneralSecurityException gse) {
			gse.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<User> getAllUsers() {
		Session s = sessionFactory.getCurrentSession();
		TypedQuery<User> query = s.createQuery("from User", User.class);
        
        return query.getResultList();
	}

	@Override
	public User getUser(String username) {
		Session s = sessionFactory.getCurrentSession();
		TypedQuery<User> query = s.createQuery("from User where username = :username", User.class);
		query.setParameter("username", username);
        
        List<User> l = query.getResultList();
        
        if (l == null || l.size() == 0) {
        	return null;
        }
        
        return l.get(0);
	}

	@Override
	public void delete(String username) {
		try {
			User u = new User();
			u.setUsername(username);
			
			sessionFactory.getCurrentSession().delete(u);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean validate(String username, String password) {
		User u = getUser(username);
		
		// user not found
		if (u == null) {
			return false;
		}
		
		return u.getPassword().equals(password);
	}
	
}
