package com.company.inventory.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.company.inventory.model.Category;

public interface ICategoryDao extends CrudRepository<Category, Long>{
	@Query("SELECT c FROM Category c WHERE c.name LIKE %?1%")
	List<Category> findByNameLike(String name);
	List <Category> findByNameContainingIgnoreCase(String name);
}
