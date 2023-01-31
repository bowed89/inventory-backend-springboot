package com.company.inventory.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.company.inventory.model.Product;

public interface IProductDao extends CrudRepository<Product, Long>{

	@Query("SELECT p FROM Product p WHERE p.name LIKE %?1%")
	List<Product> findByNameLike(String name);
	// findByNameContainingIgnoreCase => es similar al query de arriba
	// es una funcion de Springboot donde las mayusculas lo ignora y busca por name
	List<Product> findByNameContainingIgnoreCase(String name);


}
