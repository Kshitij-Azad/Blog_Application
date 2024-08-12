package com.pro.controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.pro.entity.User;
import com.pro.helper.Message;
import com.pro.repo.UserRepository;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;




@Controller
public class HomeController {

	

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	/* LOGIN */
	@RequestMapping("/")
	public String l(Model model)
	{
		model.addAttribute("title", "LOGIN--E_commerce");
		return "login";		
	}
	@RequestMapping("/login")
	public String login(Model model)
	{
		model.addAttribute("title", "LOGIN--E_commerce");
		return "login";		
	}
	
	/* Create new Account */
	@RequestMapping("/register")
	public String Register(Model model)
	{
		model.addAttribute("title", "Register--E_commerce");
		model.addAttribute("user",new User());
		return "register";
	}
	
	/* Handle for register */
	@RequestMapping(value = "/do_register", method = RequestMethod.POST)
	public String registerUser(@Valid @ModelAttribute("user") User user, Model model, BindingResult result1, HttpSession session) {
	    try {
	        
	        if(result1.hasErrors())
			{
				System.out.println("ERROR"+result1.toString());
				model.addAttribute("user",user);
				return "register";
			}

	        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
	        System.out.println("User: " + user);
	        user.setRole("ROLE_USER");
	        User result = this.userRepository.save(user);
	        
	        model.addAttribute("user", new User());
	        session.setAttribute("message", new Message("Successfully Registered !!", "alert-success"));
//	        return "redirect:/login"; // Redirect to login page after successful registration
	        return "login";
	    } catch (Exception e) {
	        e.printStackTrace();
	        model.addAttribute("user", user);
	        session.setAttribute("message", new Message("Something Went wrong !! " + e.getMessage(), "alert-danger"));
	        return "register"; // Return to registration page with error message
	    }
	}
		
}
