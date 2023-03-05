package com.company.inventory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.company.inventory.model.Contact;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Integer> {
	
}
