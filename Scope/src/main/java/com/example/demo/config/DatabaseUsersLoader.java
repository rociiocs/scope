package com.example.demo.config;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.demo.models.User;
import com.example.demo.repositories.UserRepository;

 
@Component
public class DatabaseUsersLoader {
 
    @Autowired
    private UserRepository users;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
 
    @CacheEvict
    @PostConstruct
    private void initDatabase() {
    	if(users.count()==0) {
        	users.save(new User("user@gmail.com","Usuario","Usuario","Scope","Developer", passwordEncoder.encode("pass"), "USER"));
        	users.save(new User("admin@gmail.com","Admin","Admin","Scope","Director", passwordEncoder.encode("adminpass"), "USER", "ADMIN"));
        }
       
    }
}