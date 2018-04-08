package com.onlinestore.backend.dao;

import java.util.List;

import com.onlinestore.backend.model.Cart;
import com.onlinestore.backend.model.Product;

public interface CartDAO {

	void saveOrUpdate(Cart c);
	
	List<Cart> getAllCarts();
	
	List<Cart> getAllCarts(String username);
	
	Cart getCart(int id);
	
	void delete(int id);
	
	void addProduct(Cart c, Product p);
}
