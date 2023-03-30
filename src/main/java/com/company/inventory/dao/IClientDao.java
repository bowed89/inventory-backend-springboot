package com.company.inventory.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.company.inventory.model.Client;

public interface IClientDao extends CrudRepository<Client, Long> {
	@Query("SELECT s FROM Client s WHERE s.name LIKE %?1%")
	List<Client> findByNameLike(String name);
	List <Client> findByNameContainingIgnoreCase(String name);

}
