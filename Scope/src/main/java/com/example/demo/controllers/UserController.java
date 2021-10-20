package com.example.demo.controllers;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.demo.models.User;
import com.example.demo.repositories.UserRepository;


@Controller
public class UserController {

	@Autowired
	private UserRepository users;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@GetMapping("/signin")
	public String login(Model model, HttpServletRequest request) {
		CsrfToken token = (CsrfToken) request.getAttribute("_csrf");
		 model.addAttribute("token", token.getToken());
		return "signin";
	}
	

	@GetMapping("/user_loged")
	public String userLoged(Model model, HttpServletRequest request) {

		String name = request.getUserPrincipal().getName();
		
		User user = users.findByName(name).orElseThrow();

		model.addAttribute("usernombre", user.getName());		
		model.addAttribute("user", user.getName());		
		model.addAttribute("admin", request.isUserInRole("ADMIN"));
		return "user_loged";
	}
	
	@GetMapping("/signout")
	public String logout() {
		return "signout";
	}
	
	@GetMapping("/signup")
	public String signUp(){
		return "signup";
	}

	@PostMapping("/signup")
	public String createUser(Model model, 
			@RequestParam String name, 
			@RequestParam String lastname, 
			@RequestParam String mail, 
			@RequestParam String password,
			@RequestParam String company,
			@RequestParam String password2,
			@RequestParam String occupation) {
		Optional<User> userold = users.findByMail(mail);
		System.out.print("Hola he entrado");
		if (userold.isPresent()) {
			model.addAttribute("error2",true);
			System.out.print("Error");
			return "signup";
		}else {
			System.out.print("ME VOY POR EL ELSE");
			if(password.equals(password2)) {
				System.out.print("Holaaa");
				User new_User = new User(mail,name,lastname,company,occupation,passwordEncoder.encode(password),"USER");
				users.save(new_User);
				model.addAttribute("user",new_User.getName());
				return "signin";
			}else {
				System.out.print("Error no coinciden contraseñas");
				model.addAttribute("error",true);
				return "signup";
			}
		}
	}
	
}
