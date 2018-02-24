package onlinestore.backend.dao;

import java.util.List;

import onlinestore.backend.model.Seller;

public interface SellerDAO {

	void saveOrUpdate(Seller s);
	
	List<Seller> getAllSellers();
	
	Seller getSeller(int id);
	
	void delete(int id);
	
}
