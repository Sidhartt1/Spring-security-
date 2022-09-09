package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Users;

@Repository
public interface UserRepo extends JpaRepository<Users, Integer> 
{

	Users findByEmail(String email);
	
}