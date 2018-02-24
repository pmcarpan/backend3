package onlinestore.backend.dao;

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

import onlinestore.backend.model.User;

@Repository
@Transactional
public class UserDAOImpl implements UserDAO {

	@Autowired
	private SessionFactory sessionFactory;

	public void saveOrUpdate(User u) {
		sessionFactory.getCurrentSession().saveOrUpdate(u);	
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
