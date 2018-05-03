package com.onlinestore.backend.dao;

import java.util.List;

import javax.persistence.TypedQuery;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

// import com.onlinestore.backend.model.Product;
import com.onlinestore.backend.model.Seller;

@Repository
@Transactional
public class SellerDAOImpl implements SellerDAO {

	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	public void saveOrUpdate(Seller s) {
		s.setEnabled(true);
		sessionFactory.getCurrentSession().saveOrUpdate(s);
	}

	@Override
	public List<Seller> getAllSellers() {
		Session s = sessionFactory.getCurrentSession();
		TypedQuery<Seller> query = s.createQuery("from Seller where enabled = true", Seller.class);
        
        return query.getResultList();
	}

	@Override
	public Seller getSeller(int id) {
		Session s = sessionFactory.getCurrentSession();
		TypedQuery<Seller> query = s.createQuery("from Seller where id = :id", Seller.class);
        query.setParameter("id", id);
		
        List<Seller> l = query.getResultList();
        
        if (l == null || l.size() == 0) {
        	return null;
        }
        
        return l.get(0);
	}

	@Override
	public void delete(int id) {
		Session sess = sessionFactory.getCurrentSession();
		Seller s = getSeller(id);
		s.setEnabled(false);
		sess.saveOrUpdate(s);
	}

}
