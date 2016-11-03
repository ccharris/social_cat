package com.harris.carolyn.lab302.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.harris.carolyn.lab302.beans.Contact;
import com.harris.carolyn.lab302.beans.Email;
import com.harris.carolyn.lab302.beans.EmailService;
import com.harris.carolyn.lab302.beans.User;
import com.harris.carolyn.lab302.beans.UserRole;
import com.harris.carolyn.lab302.repository.ContactRepository;
import com.harris.carolyn.lab302.repository.UserRepository;
import com.harris.carolyn.lab302.repository.UserRoleRepository;


@Controller
public class ContactController {
	private static final Logger log = LoggerFactory.getLogger(ContactController.class);

	@Autowired
	private ContactRepository contactRepo;
	@Autowired
	private UserRoleRepository userRoleRepo;
	@Autowired
	private UserRepository userRepo;
//	
//	@GetMapping("/contact/search")
//	public String contactSearch(Model model, @RequestParam(name = "srch-term", required = false) String searchTerm) {
//		if(searchTerm == null || "".equals(searchTerm)){
//			model.addAttribute("contacts", contactRepo.findAll());
//		} else {
//			model.addAttribute("contacts", contactRepo.findByLastNameContainsOrFirstNameContainsOrEmailContainsOrPhoneNumberContainsAllIgnoreCase(searchTerm, searchTerm, searchTerm, searchTerm));
//		}
//		return "contacts";
//	}
	
	@GetMapping("/contacts")
	public String home(Model model, @RequestParam(name = "srch-term", required = false) String searchTerm) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String name = auth.getName();
		User v = userRepo.findOneByEmail(name);
		boolean isUser = false;
		for (UserRole ur : v.getUserRoles()){
			if (ur.getRole().equals("USER"))
				isUser = true;
		}
		model.addAttribute("isUser", isUser);
		
		if(searchTerm == null || "".equals(searchTerm)){
			model.addAttribute("contacts", contactRepo.findByUserId(v.getId()));
		} else {
			List<Contact> userContacts = contactRepo.findByUserId(v.getId());
			List<Contact> searchContacts = contactRepo.findByLastNameContainsOrFirstNameContainsOrEmailContainsOrPhoneNumberContainsAllIgnoreCase(searchTerm, searchTerm, searchTerm, searchTerm);
			List<Contact> contacts = new ArrayList<Contact>();
			for (Contact contact : searchContacts) {
				if (userContacts.contains(contact)) {
					contacts.add(contact);
				}
			}
			model.addAttribute("contacts", contacts);
			
		}
		
		return "contacts";
	}

	@GetMapping("/contact/{id}")
	public String contact(Model model, @PathVariable(name = "id") long id) {
		model.addAttribute("id", id);
		Contact u = contactRepo.findOne(id);
		model.addAttribute("contact", u);
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String name = auth.getName();
		User v = userRepo.findOneByEmail(name);
		long userId = v.getId();
		model.addAttribute("userId", userId);
		boolean isUser = false;
		for (UserRole ur : v.getUserRoles()){
			if (ur.getRole().equals("USER"))
				isUser = true;
		}
		model.addAttribute("isUser", isUser);
		return "contact_detail";
	}

	@GetMapping("/contact/{id}/edit")
	public String contactEdit(Model model, @PathVariable(name = "id") long id) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String name = auth.getName();
		User v = userRepo.findOneByEmail(name);
		boolean isUser = false;
		for (UserRole ur : v.getUserRoles()){
			if (ur.getRole().equals("USER"))
				isUser = true;
		}
		long userId = v.getId();
		model.addAttribute("userId", userId);
		model.addAttribute("isUser", isUser);
		model.addAttribute("id", id);
		Contact u = contactRepo.findOne(id);
		model.addAttribute("contact", u);
		return "contact_edit";
	}

	@PostMapping("/contact/{id}/edit")
	public String contactEditSave(@PathVariable(name = "id") long id, @ModelAttribute @Valid Contact contact,
			BindingResult result, Model model) {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String name = auth.getName();
		User v = userRepo.findOneByEmail(name);
		long userId = v.getId();
		model.addAttribute("userId", userId);
		boolean isUser = false;
		for (UserRole ur : v.getUserRoles()){
			if (ur.getRole().equals("USER"))
				isUser = true;
		}
		model.addAttribute("isUser", isUser);

		if (result.hasErrors()) {
			model.addAttribute("contact", contact);
			return "contact_edit";
		} else {
			contact.setUser(v);
			contactRepo.save(contact);
			
			return "redirect:/contact/" + contact.getId();
		}

	}

	@GetMapping("/contact/{id}/delete")
	public String contactDelete(Model model, @PathVariable(name = "id") long id) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String name = auth.getName();
		User v = userRepo.findOneByEmail(name);
		long userId = v.getId();
		model.addAttribute("userId", userId);
		boolean isUser = false;
		for (UserRole ur : v.getUserRoles()){
			if (ur.getRole().equals("USER"))
				isUser = true;
		}
		model.addAttribute("isUser", isUser);
		model.addAttribute("id", id);
		Contact u = contactRepo.findOne(id);
		model.addAttribute("contact", u);
		return "contact_delete";
	}

	@PostMapping("/contact/{id}/delete")
	public String contactDeleteSave(@PathVariable(name = "id") long id, @ModelAttribute @Valid Contact contact,
			BindingResult result, Model model) {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String name = auth.getName();
		User v = userRepo.findOneByEmail(name);
		long userId = v.getId();
		model.addAttribute("userId", userId);
		boolean isUser = false;
		for (UserRole ur : v.getUserRoles()){
			if (ur.getRole().equals("USER"))
				isUser = true;
		}
		model.addAttribute("isUser", isUser);
		contactRepo.delete(contact);
		return "redirect:/contacts";

	}
	
	@GetMapping("/email/contact/{id}")
	public String email(Model model, @PathVariable(name = "id") long id) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String name = auth.getName();
		User v = userRepo.findOneByEmail(name);
		Contact u = contactRepo.findOne(id);
		String emailAdd = u.getEmail();
		model.addAttribute("emailAdd", emailAdd);
		boolean isAddress = true;
		model.addAttribute("isAddress", isAddress);
		model.addAttribute("user", v);
		Email e = new Email();
		model.addAttribute("email", e);
		return "send_mail";
	}

	@PostMapping("/email/contact/{id}")
	public String emailSend(@ModelAttribute @Valid Email email, @PathVariable(name = "id") long id, BindingResult result, Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String name = auth.getName();
		User v = userRepo.findOneByEmail(name);
		String emailAdd = contactRepo.findOne(id).getEmail();
		email.setAddress(emailAdd);
		EmailService es = new EmailService();
		es.sendEmail(email, v);
		boolean isAdmin = false;
		boolean isUser = false;
		for (UserRole ur : v.getUserRoles()){
			if (ur.getRole().equals("USER")){
				isUser = true;
			} else if (ur.getRole().equals("ADMIN")){
				isAdmin = true;
			}
		}
		model.addAttribute("isUser", isUser);
		model.addAttribute("isAdmin", isAdmin);
		return "email_success";
	}

	@GetMapping("/contact/create")
	public String contactCreate(Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String name = auth.getName();
		User v = userRepo.findOneByEmail(name);
		boolean isUser = false;
		long userId = v.getId();
		model.addAttribute("userId", userId);
		for (UserRole ur : v.getUserRoles()){
			if (ur.getRole().equals("USER"))
				isUser = true;
		}
		model.addAttribute("isUser", isUser);
		model.addAttribute(new Contact());
		
		return "contact_create";
	}

	@PostMapping("/contact/create")
	public String contactCreateSave(@ModelAttribute @Valid Contact contact, BindingResult result, Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String name = auth.getName();
		User v = userRepo.findOneByEmail(name);
		long userId = v.getId();
		model.addAttribute("userId", userId);
		boolean isUser = false;
		for (UserRole ur : v.getUserRoles()){
			if (ur.getRole().equals("USER"))
				isUser = true;
		}
		model.addAttribute("isUser", isUser);

		if (result.hasErrors()) {
			model.addAttribute("contact", contact);
			return "contact_create";
		} else {
			contact.setUser(v);
			contactRepo.save(contact);
			return "redirect:/contacts";
		}

	}
	
}
