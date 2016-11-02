package com.harris.carolyn.lab302.controller;

import java.io.IOException;

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
import org.springframework.web.multipart.MultipartFile;

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
	public String users(Model model) {
		model.addAttribute("users", userRepo.findAll());
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String name = auth.getName();
		User v = userRepo.findOneByEmail(name);
		boolean isAdmin = false;
		for (UserRole ur : v.getUserRoles()){
			if (ur.getRole().equals("ADMIN"))
				isAdmin = true;
		}
		model.addAttribute("isAdmin", isAdmin);
		return "users";
	}

	@GetMapping("/login")
	public String login(Model model) {

		return "login";
	}

	@PostMapping("/login")
	public String loginSubmit() {
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

}
