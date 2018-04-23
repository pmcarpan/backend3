package com.onlinestore.backend.dao;

import com.onlinestore.backend.model.OrderDetail;

public interface OrderDetailDAO {

	public void saveOrUpdate(OrderDetail oD, String username);
	
	public void delete(int id);
	
}
