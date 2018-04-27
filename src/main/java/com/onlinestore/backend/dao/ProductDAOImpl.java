package com.onlinestore.backend.dao;

import java.io.File;
import java.io.IOException;
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

	private static final String IMAGEFOLDER = 
			"C:\\Users\\Supratik basu\\git\\frontend3\\src\\main\\webapp\\resources\\images\\products\\";
	
	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private CategoryDAO cDAO;
	@Autowired
	private SellerDAO sDAO;
	
	public void saveOrUpdate(Product p) {
		Product p1 = getProduct(p.getId());
		Seller s;
		Category c;
		
		if (p1 == null) {
			save(p);
			storeImage(p); // after save, p has some id != 0
			
			System.out.println(p.getId());
//			System.out.println("Sellers : " + s.getProducts());
//			System.out.println("Categories : " + c.getProducts());
		}
		else {
			update(p);
		}
		
	}

	private void storeImage(Product p) {
		String ext = p.getImage().getOriginalFilename();
		ext = "." + ext.substring(ext.lastIndexOf(".") + 1);
		
		File f = new File(IMAGEFOLDER + "Product" + p.getId() + ext);
		
		p.setImageAddress("Product" + p.getId() + ext);
		
		try {
			p.getImage().transferTo(f);
		} 
		catch (IllegalStateException e) {
			e.printStackTrace();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void save(Product p) {
		Seller s;
		Category c;
		
		s = sDAO.getSeller(p.getSellerId());
		p.setSeller(s);
		s.getProducts().add(p);
		
		c = cDAO.getCategory(p.getCategoryId());
		p.setCategory(c);
		c.getProducts().add(p);
		
		sessionFactory.getCurrentSession().saveOrUpdate(p);
	}
	
	private void update(Product p) {
		Product p1 = getProduct(p.getId());
		Seller s;
		Category c;
		
		p1.setName(p.getName());
		p1.setDescription(p.getDescription());
		p1.setPrice(p.getPrice());
		
		if (p1.getCategoryId() != p.getCategoryId()) {
			c = cDAO.getCategory(p1.getCategoryId());
			c.getProducts().remove(p1);
			
			p1.setCategoryId(p.getCategoryId());
			c = cDAO.getCategory(p1.getCategoryId());
			p1.setCategory(c);
			c.getProducts().add(p1);
		}
		
		if (p1.getSellerId() != p.getSellerId()) {
			s = sDAO.getSeller(p1.getSellerId());
			s.getProducts().remove(p1);
			
			p1.setSellerId(p.getSellerId());
			s = sDAO.getSeller(p1.getSellerId());
			p1.setSeller(s);
			s.getProducts().add(p1);
		}
		
		p1.setImage(p.getImage());
		if (p1.getImage().getSize() != 0) {
			storeImage(p1);
		}
		
		sessionFactory.getCurrentSession().saveOrUpdate(p1);
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
		try {
			System.out.println("\nProductDAOImpl delete()");
			
			Product p = getProduct(id);
			Seller s = p.getSeller();
			Category c = p.getCategory();
			
//			System.out.println("Sellers : " + s.getProducts());
//			System.out.println("Categories : " + c.getProducts());
			
			s.getProducts().remove(p);
			c.getProducts().remove(p);
			
			// p.setSeller(null);
			// p.setCategory(null);
			
//			System.out.println("Sellers : " + s.getProducts());
//			System.out.println("Categories : " + c.getProducts());
			
			sessionFactory.getCurrentSession().delete(p);
		} catch (Exception e) {
			System.out.println("ProductDAOIMPL delete()");
			e.printStackTrace();
		}
	}

}
