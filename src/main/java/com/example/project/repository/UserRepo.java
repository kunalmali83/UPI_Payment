package com.example.project.repository;


import java.util.Optional;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.project.entities.User;

public interface  UserRepo extends JpaRepository<User,Long>{

	 Optional<User> findByEmail(String email);

	boolean existsByEmail(String email);

	Optional<User> findById(Long userId);

	Optional<User> findByAccountsAccountNumber(String accountNumber);



	

	
}
