package onlinestore.backend.dao;

import java.util.List;

import onlinestore.backend.model.Category;

public interface CategoryDAO {

	void saveOrUpdate(Category c);
	
	List<Category> getAllCategories();
	
	Category getCategory(int id);
	
	void delete(int id);
}
