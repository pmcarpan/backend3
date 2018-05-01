package com.onlinestore.backend.dao;

import java.util.List;

import com.onlinestore.backend.model.OrderDetail;

public interface OrderDetailDAO {

	public void saveOrUpdate(OrderDetail oD, String username);
	
	public List<OrderDetail> getAllOrders();
	
	public OrderDetail getOrderById(int id);
	
	public void delete(int id);
	
}
