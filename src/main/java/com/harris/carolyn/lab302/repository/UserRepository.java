package com.harris.carolyn.lab302.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.harris.carolyn.lab302.beans.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

	User findOneByEmail(String email);
	
}
