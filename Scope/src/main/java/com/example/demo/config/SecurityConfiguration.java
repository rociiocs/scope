package com.example.demo.config;

import java.security.SecureRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class SecurityConfiguration  extends WebSecurityConfigurerAdapter{
	@Autowired
	RepositoryUserDetailsService userDetailsService;
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(10, new SecureRandom());
	}
	@Override
	 protected void configure(HttpSecurity http) throws Exception {
		
	 // Public pages
	 http.authorizeRequests().antMatchers("/").permitAll();
	 http.authorizeRequests().antMatchers("/signin").permitAll();
	 http.authorizeRequests().antMatchers("/signup").permitAll();
	 http.authorizeRequests().antMatchers("/signout").permitAll();
	 
	 // Private pages (all other pages)
	 //Productos
	 http.authorizeRequests().antMatchers("/streaming/{id}").hasAnyRole("USER"); 
	 http.authorizeRequests().antMatchers("/products/{id}/delete_product").hasAnyRole("ADMIN"); 
	 
	 // Login form
	 http.formLogin().loginPage("/signin");
	 http.formLogin().usernameParameter("username");
	 http.formLogin().passwordParameter("password");
	 http.formLogin().defaultSuccessUrl("/users/user_loged");
	 http.formLogin().failureUrl("/signin");
	 
	 // Logout
	 http.logout().logoutUrl("/signout");
	 http.logout().logoutSuccessUrl("/");

	 // Disable CSRF at the moment
	 //http.csrf().disable();
	 }
	
	@Override
	 protected void configure(AuthenticationManagerBuilder auth) throws Exception {
	 auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	 }
	
}
