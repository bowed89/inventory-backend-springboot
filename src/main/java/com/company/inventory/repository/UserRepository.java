package com.company.inventory.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.company.inventory.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
	Optional<User> findOneByEmail(String email);
}
