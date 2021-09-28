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
	private String alias;
	private String password;
	private String mail;
	private Date birth;
	private String gender;
	
	@ElementCollection(fetch = FetchType.EAGER)
	private List<String> roles= new ArrayList<String>();
	
	/*
	@JsonIgnore
	@OneToMany(mappedBy="usuario",cascade=CascadeType.ALL,orphanRemoval=true)
	private List<Comment>comments = new ArrayList<Comment>();
	*/
	@JsonIgnore
	@ManyToMany
	private List<Video> favouriteVideos = new ArrayList<Video>();
	
	public User() {
	}
	
	public User(String name, String password, String... roles) {
		this.name = name;
		this.password = password;
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
	
	public String getAlias() {
		return alias;
	}
	
	public void setAlias(String alias) {
		this.alias = alias;
	}
	
	public Date getBirth() {
		return birth;
	}
	
	public void setBirth(Date birth) {
		this.birth = birth;
	}
	
	public String getGender() {
		return gender;
	}
	
	public void setGender(String gender) {
		this.gender = gender;
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
}
