package com.onlinestore.backend.dao;

import java.util.List;

import com.onlinestore.backend.model.Seller;

public interface SellerDAO {

	void saveOrUpdate(Seller s);
	
	List<Seller> getAllSellers();
	
	Seller getSeller(int id);
	
	void delete(int id);
	
}
