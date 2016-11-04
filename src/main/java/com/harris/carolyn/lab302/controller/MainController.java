package com.harris.carolyn.lab302.controller;

import java.io.IOException;

import javax.inject.Inject;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.TwitterProfile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.harris.carolyn.lab302.beans.Email;
import com.harris.carolyn.lab302.beans.EmailService;
import com.harris.carolyn.lab302.beans.User;
import com.harris.carolyn.lab302.beans.UserImage;
import com.harris.carolyn.lab302.beans.UserRole;
import com.harris.carolyn.lab302.repository.UserImageRepository;
import com.harris.carolyn.lab302.repository.UserPropertyRepository;
import com.harris.carolyn.lab302.repository.UserRepository;
import com.harris.carolyn.lab302.repository.UserRoleRepository;

@Controller
public class MainController {
	private static final Logger log = LoggerFactory.getLogger(MainController.class);

	@Autowired
	private UserRepository userRepo;
	@Autowired
	private UserImageRepository userImgRepo;
	@Autowired
	private UserPropertyRepository userPropRepo;
	@Autowired
	private UserRoleRepository userRoleRepo;

	@GetMapping("")
	public String index(Model model) {

		model.addAttribute("users", userRepo.findAll());
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String name = auth.getName();
		User v = userRepo.findOneByEmail(name);
		if (v == null){
			return "redirect:/login";
		} else {
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
		return "index";
		}
	}
	
	@GetMapping("/home")
	public String home(Model model) {
		model.addAttribute("users", userRepo.findAll());
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String name = auth.getName();
		User v = userRepo.findOneByEmail(name);
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
		if (isAdmin){
			model.addAttribute("users", userRepo.findAll());
			return "users";
		} else {
			model.addAttribute("user", v);
			model.addAttribute("users", userRepo.findAll());
			return "contacts";
		}
	}
	
	@GetMapping("/signup")
	public String signup(Model model) {
		model.addAttribute(new User());
		return "signup";
	}

	@PostMapping("signup")
	public String signupSave(@ModelAttribute @Valid User user,
			BindingResult result, Model model) {

		if (result.hasErrors()) {
			model.addAttribute("user", user);
			return "signup";
		} else {
			userRepo.save(user);
			UserRole ur = new UserRole(user);
			userRoleRepo.save(ur);
			return "redirect:/";
		}

	}

	@GetMapping("/users")
	public String users(Model model, @RequestParam(name = "srch", required = false) String searchTerm) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String name = auth.getName();
		User v = userRepo.findOneByEmail(name);
		boolean isAdmin = false;
		for (UserRole ur : v.getUserRoles()){
			if (ur.getRole().equals("ADMIN"))
				isAdmin = true;
		}
		model.addAttribute("isAdmin", isAdmin);
		
		if (searchTerm == null || "".equals(searchTerm)){
		
		model.addAttribute("users", userRepo.findAll());
		} else {
			model.addAttribute("users", userRepo.findByFullNameContainsOrEmailContainsOrPhoneNumberContainsAllIgnoreCase( searchTerm, searchTerm, searchTerm));
		}
		
		return "users";
	}

	
	@GetMapping("/login")
	public String login(Model model) {

		return "login";
		
	}

	@PostMapping("/login")
	public String loginSubmit(Model model) {
		
		return "index";
	}
	


	@GetMapping("/user/{id}")
	public String user(Model model, @PathVariable(name = "id") long id) {
		model.addAttribute("id", id);
		User u = userRepo.findOne(id);
		model.addAttribute("user", u);
		UserImage i = userImgRepo.findByUserId(id);
		model.addAttribute("userImage", i);
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String name = auth.getName();
		User v = userRepo.findOneByEmail(name);
		boolean isAdmin = false;
		for (UserRole ur : v.getUserRoles()){
			if (ur.getRole().equals("ADMIN"))
				isAdmin = true;
		}
		model.addAttribute("isAdmin", isAdmin);
		return "user_detail";
	}

	@GetMapping("/user/{id}/edit")
	public String userEdit(Model model, @PathVariable(name = "id") long id) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String name = auth.getName();
		User v = userRepo.findOneByEmail(name);
		boolean isAdmin = false;
		for (UserRole ur : v.getUserRoles()){
			if (ur.getRole().equals("ADMIN"))
				isAdmin = true;
		}
		model.addAttribute("isAdmin", isAdmin);
		model.addAttribute("id", id);
		User u = userRepo.findOne(id);
		model.addAttribute("user", u);
		return "user_edit";
	}

	@PostMapping("/user/{id}/edit")
	public String userEditSave(@PathVariable(name = "id") long id, @ModelAttribute @Valid User user,
			BindingResult result, Model model, @RequestParam("file") MultipartFile file,
			@RequestParam(name = "removeImage", defaultValue = "false", required = false) boolean removeImage) {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String name = auth.getName();
		User v = userRepo.findOneByEmail(name);
		boolean isAdmin = false;
		for (UserRole ur : v.getUserRoles()){
			if (ur.getRole().equals("ADMIN"))
				isAdmin = true;
		}
		model.addAttribute("isAdmin", isAdmin);

		if (result.hasErrors()) {
			model.addAttribute("user", user);
			return "user_edit";
		} else {
			if(removeImage){
				//see if user has an img
				UserImage image = userImgRepo.findByUserId(user.getId());
				if(image != null){
					userImgRepo.delete(image);
					log.info("Image Removed " + id);
				}
				//remove if it exists
			}

			else if (file != null && !file.isEmpty()) {
				try {
					UserImage image = userImgRepo.findByUserId(user.getId());

					if (image == null) {
						image = new UserImage();
						image.setUserId(user.getId());
					}

					image.setContentType(file.getContentType());
					image.setImage(file.getBytes());
					userImgRepo.save(image);

				} catch (IOException e) {
					log.error("Failed to uplaod file.", e);
				}
			}
			userRepo.save(user);
			return "redirect:/user/" + user.getId();
		}

	}

	@GetMapping("/user/{id}/delete")
	public String userDelete(Model model, @PathVariable(name = "id") long id) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String name = auth.getName();
		User v = userRepo.findOneByEmail(name);
		boolean isAdmin = false;
		for (UserRole ur : v.getUserRoles()){
			if (ur.getRole().equals("ADMIN"))
				isAdmin = true;
		}
		model.addAttribute("isAdmin", isAdmin);
		model.addAttribute("id", id);
		User u = userRepo.findOne(id);
		model.addAttribute("user", u);
		return "user_delete";
	}

	@PostMapping("/user/{id}/delete")
	public String userDeleteSave(@PathVariable(name = "id") long id, @ModelAttribute @Valid User user,
			BindingResult result, Model model) {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String name = auth.getName();
		User v = userRepo.findOneByEmail(name);
		boolean isAdmin = false;
		for (UserRole ur : v.getUserRoles()){
			if (ur.getRole().equals("ADMIN"))
				isAdmin = true;
		}
		model.addAttribute("isAdmin", isAdmin);
		userRepo.delete(user);
		return "redirect:/users";

	}

	@GetMapping("/user/create")
	public String userCreate(Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String name = auth.getName();
		User v = userRepo.findOneByEmail(name);
		boolean isAdmin = false;
		for (UserRole ur : v.getUserRoles()){
			if (ur.getRole().equals("ADMIN"))
				isAdmin = true;
		}
		model.addAttribute("isAdmin", isAdmin);
		model.addAttribute(new User());
		return "user_create";
	}

	@PostMapping("/user/create")
	public String userCreateSave(@ModelAttribute @Valid User user, BindingResult result, Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String name = auth.getName();
		User v = userRepo.findOneByEmail(name);
		boolean isAdmin = false;
		for (UserRole ur : v.getUserRoles()){
			if (ur.getRole().equals("ADMIN"))
				isAdmin = true;
		}
		model.addAttribute("isAdmin", isAdmin);

		if (result.hasErrors()) {
			model.addAttribute("user", user);
			return "user_create";
		} else {
			userRepo.save(user);
			UserRole ur = new UserRole(user);
			userRoleRepo.save(ur);
			return "redirect:/users";
		}

	}
	
	@GetMapping("/profile")
	public String profile(Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String name = auth.getName();
		User v = userRepo.findOneByEmail(name);
		model.addAttribute("user", v);
		UserImage i = userImgRepo.findByUserId(v.getId());
		model.addAttribute("userImage", i);
		return "profile";
	}

	@GetMapping("/profile/edit")
	public String profileEdit(Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String name = auth.getName();
		User v = userRepo.findOneByEmail(name);
		model.addAttribute("id", v.getId());
		model.addAttribute("user", v);
		return "profile_edit";
	}

	@PostMapping("/profile/edit")
	public String profileEditSave( @ModelAttribute @Valid User user,
			BindingResult result, Model model, @RequestParam("file") MultipartFile file,
			@RequestParam(name = "removeImage", defaultValue = "false", required = false) boolean removeImage) {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String name = auth.getName();
		User v = userRepo.findOneByEmail(name);
		long id = v.getId();

		if (result.hasErrors()) {
			model.addAttribute("user", user);
			return "profile_edit";
		} else {
			if(removeImage){
				//see if user has an img
				UserImage image = userImgRepo.findByUserId(user.getId());
				if(image != null){
					userImgRepo.delete(image);
					log.info("Image Removed " + id);
				}
				//remove if it exists
			}

			else if (file != null && !file.isEmpty()) {
				try {
					UserImage image = userImgRepo.findByUserId(user.getId());

					if (image == null) {
						image = new UserImage();
						image.setUserId(user.getId());
					}

					image.setContentType(file.getContentType());
					image.setImage(file.getBytes());
					userImgRepo.save(image);

				} catch (IOException e) {
					log.error("Failed to uplaod file.", e);
				}
			}
			userRepo.save(user);
			return "redirect:/profile";
		}

	}
	
	
	
	@GetMapping("/email")
	public String email(Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String name = auth.getName();
		User v = userRepo.findOneByEmail(name);
		model.addAttribute("user", v);
		Email e = new Email();
		model.addAttribute("email", e);
		return "send_mail";
	}

	@PostMapping("/email")
	public String emailSend(@ModelAttribute @Valid Email email, BindingResult result, Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String name = auth.getName();
		User v = userRepo.findOneByEmail(name);
		email.setMessage((email.getMessage() + " \nSign up for Social Cat! \nhttp://socialcat.com"));
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
	@GetMapping("/user/search")
	public String userSearch(Model model) {
		model.addAttribute("users", userRepo.findAll());
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String name = auth.getName();
		User v = userRepo.findOneByEmail(name);
		boolean isUser = false;
		for (UserRole ur : v.getUserRoles()){
			if (ur.getRole().equals("USER"))
				isUser = true;
		}
		model.addAttribute("isUser", isUser);
		model.addAttribute("user", v);
		return "users";
	}
	
	
	@GetMapping("/pw")
	public String pwRecover(Model model) {
		model.addAttribute("users", userRepo.findAll());
		return "pw_recovery";
	}

	@PostMapping("/pw")
	public String pwRecoverSave(Model model, @RequestParam(name = "email", required = true) String email){
		User u = userRepo.findOneByEmail(email);
		model.addAttribute("user", u);
		long id = u.getId();
		model.addAttribute("id", id);
		return"redirect:/pw/" + id;
	}
	@GetMapping("/pw/{id}")
	public String pwRecoverGet(@PathVariable(name = "id") long id, Model model){
		model.addAttribute("user", userRepo.findOne(id));
		model.addAttribute("id", id);
		return"pw_recovery_two";
	}

	@PostMapping("/pw/{id}")
	public String pwRecoverShow(@PathVariable(name = "id") long id,  @RequestParam(name = "secretAnswer", required = false) String secretAnswer, Model model){
		User u = userRepo.findOne(id);
		if(secretAnswer.equalsIgnoreCase(u.getSecretAnswer())){
			model.addAttribute(u.getPassword());
			model.addAttribute("user", u);
		} else {
			model.addAttribute("user", u);
			return "pw_recovery_two";
		}
		
		return"pw_recover_three";
	}

	
}
