package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.PasswordVerifyToken;

@Repository
public interface PasswordRepo extends JpaRepository<PasswordVerifyToken, Integer> 
{

	PasswordVerifyToken findByToken(String token);
	
}