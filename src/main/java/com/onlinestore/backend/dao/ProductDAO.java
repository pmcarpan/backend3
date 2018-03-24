package com.onlinestore.backend.dao;

import java.util.List;

import com.onlinestore.backend.model.Product;

public interface ProductDAO {

	void saveOrUpdate(Product p);
	
	List<Product> getAllProducts();
	
	List<Product> getAllProductsByCategory(int categoryId);
	
	List<Product> getAllProductsBySeller(int sellerId);
	
	Product getProduct(int id);
	
	void delete(int id);

}
