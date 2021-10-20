package com.example.demo.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	private String name;
	private String lastname;
	private String password;
	private String mail;
	private String company;
	private String occupation;
	@ElementCollection(fetch = FetchType.EAGER)
	private List<String> roles= new ArrayList<String>();
	
	/*
	@JsonIgnore
	@OneToMany(mappedBy="usuario",cascade=CascadeType.ALL,orphanRemoval=true)
	private List<Comment>comments = new ArrayList<Comment>();
	*/
	
	public User() {
	}
	/*
	public User(String name, String password, String... roles) {
		this.name = name;
		this.password = password;
		this.roles = List.of(roles);
	}
	*/
	public User(String mail, String name, String lastname, String company, String occupation, String password, String... roles) {
		this.mail = mail;
		this.name = name;
		this.lastname = lastname;
		this.password = password;
		this.company = company;
		this.occupation = occupation;
		this.roles = List.of(roles);
	}
	
	public Long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	public List<String> getRoles() {
		return this.roles;
	}
	public void setRoles(List<String> roles) {
		this.roles=roles;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getLastname() {
		return lastname;
	}
	
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public void setMail(String mail) {
		this.mail = mail;
	}
	
	public String getMail() {
		return mail;
	}
	
	public void setCompany(String company) {
		this.company = company;
	}
	
	public String getCompany() {
		return company;
	}
	
	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}
	
	public String getOccupation() {
		return occupation;
	}
}
