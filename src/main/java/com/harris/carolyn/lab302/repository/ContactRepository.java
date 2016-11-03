package com.harris.carolyn.lab302.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.harris.carolyn.lab302.beans.Contact;

@Repository
public interface ContactRepository extends CrudRepository<Contact, Long> {

	public List<Contact> findByLastNameContainsOrFirstNameContainsOrEmailContainsOrPhoneNumberContainsOrNoteContainsAllIgnoreCase(String firstNamePart, String lastNamePart, String emailPart, String phoneNumberPart, String notePart);

	public List<Contact> findByUserId(long userId);
	
}
