package com.onlinestore.backend.dao;

import java.util.List;

import com.onlinestore.backend.model.Cart;

public interface CartDAO {

	void saveOrUpdate(Cart c);
	
	List<Cart> getAllCarts();
	
	Cart getCart(int id);
	
	void delete(int id);
	
	void addProduct(String username, int productId);
	
	void calcPrice(int id);
	
	void removeProduct(String username, int productId);
}
