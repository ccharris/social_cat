package com.harris.carolyn.lab302.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.harris.carolyn.lab302.beans.Contact;
import com.harris.carolyn.lab302.beans.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

	User findOneByEmail(String email);
	public List<User> findByLastNameContainsOrFirstNameContainsOrEmailContainsOrPhoneNumberContainsAllIgnoreCase(String firstNamePart, String lastNamePart, String emailPart, String phoneNumberPart);
}
