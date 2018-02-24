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

import onlinestore.backend.model.Seller;

@Repository
@Transactional
public class SellerDAOImpl implements SellerDAO {

	@Autowired
	private SessionFactory sessionFactory;
	
	public void saveOrUpdate(Seller s) {
		sessionFactory.getCurrentSession().saveOrUpdate(s);
	}

	public List<Seller> getAllSellers() {
		Session s = sessionFactory.getCurrentSession();
		CriteriaBuilder builder = s.getCriteriaBuilder();
        CriteriaQuery<Seller> criteria = builder.createQuery(Seller.class);
        Root<Seller> root = criteria.from(Seller.class);
        criteria.select(root);
        
        Query<Seller> q = s.createQuery(criteria);
        
        return q.getResultList();
	}

	public Seller getSeller(int id) {
		Session s = sessionFactory.getCurrentSession();
		CriteriaBuilder builder = s.getCriteriaBuilder();
        CriteriaQuery<Seller> criteria = builder.createQuery(Seller.class);
        Root<Seller> root = criteria.from(Seller.class);
        criteria.select(root);
        criteria.where( builder.equal( root.get("id"), id ) );
        
        Query<Seller> q = s.createQuery(criteria);
        
        if (q == null) {
        	return null;
        }
        
        List<Seller> l = q.getResultList();
        
        if (l == null || l.size() == 0) {
        	return null;
        }
        
        return l.get(0);
	}

	public void delete(int id) {
		Seller s = new Seller();
		s.setId(id);
		sessionFactory.getCurrentSession().delete(s);
	}

}
