package onlinestore.backend.dao;

import java.util.List;

import onlinestore.backend.model.Cart;

public interface CartDAO {

	void saveOrUpdate(Cart c);
	
	List<Cart> getAllCarts();
	
	List<Cart> getAllCarts(String username);
	
	Cart getCart(int id);
	
	void delete(int id);
	
}
