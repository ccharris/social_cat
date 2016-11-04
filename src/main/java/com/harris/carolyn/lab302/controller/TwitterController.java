package com.harris.carolyn.lab302.controller;

import javax.inject.Inject;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.TwitterProfile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.harris.carolyn.lab302.beans.User;
import com.harris.carolyn.lab302.beans.UserRole;
import com.harris.carolyn.lab302.repository.UserRepository;
import com.harris.carolyn.lab302.repository.UserRoleRepository;

@Controller
@RequestMapping("/twit")
public class TwitterController {
	@Autowired
	private UserRepository userRepo;
	@Autowired
	private UserRoleRepository userRoleRepo;
    private Twitter twitter;

    private ConnectionRepository connectionRepository;

    @Inject
    public TwitterController(Twitter twitter, ConnectionRepository connectionRepository) {
        this.twitter = twitter;
        this.connectionRepository = connectionRepository;
    }

    @RequestMapping(method=RequestMethod.GET)
    public String helloTwitter(Model model) {
        if (connectionRepository.findPrimaryConnection(Twitter.class) == null) {
            return "redirect:/connect/twitter";
        }

		TwitterProfile profile = twitter.userOperations().getUserProfile();
		String fullName = profile.getName();
		String twitterName = profile.getScreenName();
		User u = new User();
		u.setTwitterHandle(twitterName);
		u.setFullName(fullName);
		model.addAttribute("user", u);
		return "twitter_signup";

	}
    
    @RequestMapping(method=RequestMethod.POST)
    	public String signupSave(@ModelAttribute @Valid User user,
    			BindingResult result, Model model) {

    		if (result.hasErrors()) {
    			model.addAttribute("user", user);
    			return "twitter_signup";
    		} else {
    			System.out.println(user.getFullName());
    			userRepo.save(user);
    			UserRole ur = new UserRole(user);
    			userRoleRepo.save(ur);
    			return "redirect:/";
    		}

    }
}
