package com.example.demo.models;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Comment {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	private String name;
	private String content;
	private Date date;
	
	@ManyToOne
	@JsonIgnore
	private Video video;
	
	public Comment() {
		
	}
	public Comment(String name, String content,Video video) {
		this.name = name;
		this.content = content;
		this.video = video;
		this.date = new Date();
	}
	
	public void setDate(Date date) {
		this.date = date;
	}
	
	public Date getDate(){
		return date;
	}
	
	public Long getId() {
        return id;
    }
	
	public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
