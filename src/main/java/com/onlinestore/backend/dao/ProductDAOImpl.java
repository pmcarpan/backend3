package com.onlinestore.backend.dao;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.persistence.TypedQuery;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.onlinestore.backend.model.Category;
import com.onlinestore.backend.model.Product;
import com.onlinestore.backend.model.Seller;

@Repository
@Transactional
public class ProductDAOImpl implements ProductDAO {

	// directory for storing images
	private static final String IMAGEFOLDER = 
			"C:\\Users\\Supratik basu\\git\\frontend3\\src\\main\\webapp\\resources\\images\\products\\";
	
	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private CategoryDAO cDAO;
	@Autowired
	private SellerDAO sDAO;
	
	@Override
	public void saveOrUpdate(Product p) {
		Product p1 = getProduct(p.getId());
		
		if (p1 == null) {
			save(p);
			storeImage(p); // after save, p has some id != 0
		}
		else {
			update(p);
		}
		
	}

	private void storeImage(Product p) {
		String ext = p.getImage().getOriginalFilename();
		ext = "." + ext.substring(ext.lastIndexOf(".") + 1);
		
		p.setImageAddress("Product" + p.getId() + ext);
		
		File f1 = new File(IMAGEFOLDER + p.getImageAddress());
		
		try {
			p.getImage().transferTo(f1);
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
		
		p.setEnabled(true);
		
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
		if (!p1.getImage().isEmpty()) {
			storeImage(p1);
		}
		
		sessionFactory.getCurrentSession().saveOrUpdate(p1);
	}
	
	@Override
	public List<Product> getAllProducts() {
		Session s = sessionFactory.getCurrentSession();
		TypedQuery<Product> query = s.createQuery("from Product where enabled = true", Product.class);
        
        return query.getResultList();
	}

	@Override
	public List<Product> getAllProductsByCategory(int categoryId) {
		Session s = sessionFactory.getCurrentSession();
		TypedQuery<Product> query = s.createQuery("from Product where enabled = true and categoryId = :id", 
													Product.class);
        query.setParameter("id", categoryId);
		
        return query.getResultList();
	}

	@Override
	public List<Product> getAllProductsBySeller(int sellerId) {
		Session s = sessionFactory.getCurrentSession();
		TypedQuery<Product> query = s.createQuery("from Product where enabled = true and sellerId = :id", 
													Product.class);
        query.setParameter("id", sellerId);
		
        return query.getResultList();
	}

	@Override
	public Product getProduct(int id) {
		Session s = sessionFactory.getCurrentSession();
		TypedQuery<Product> query = s.createQuery("from Product where id = :id", Product.class);
		query.setParameter("id", id);
        
        List<Product> l = query.getResultList();
        
        if (l == null || l.size() == 0) {
        	return null;
        }
        
        return l.get(0);
	}

	@Override
	public void delete(int id) {
		try {
			System.out.println("\nProductDAOImpl delete()");
			
			Product p = getProduct(id);
			p.setEnabled(false);
			
			sessionFactory.getCurrentSession().saveOrUpdate(p);
		} 
		catch (Exception e) {
			System.out.println("ProductDAOIMPL delete()");
			e.printStackTrace();
		}
	}

}
