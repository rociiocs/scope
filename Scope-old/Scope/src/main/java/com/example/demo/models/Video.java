package com.example.demo.models;

import java.sql.Blob;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Video {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	private String name;
	private String link;
	private String description;
	private int views;
	private int likes;
	private Date date;
	@Lob
	@JsonIgnore
	private Blob poster;
	private String image;
	
	@JsonIgnore
	@OneToMany(mappedBy="video",cascade=CascadeType.ALL, orphanRemoval=true)
	List<Comment> comments = new ArrayList<Comment>();
	
	public Video() {
		
	}
	public Video(String name,String link,Blob poster, String description) {
		this.name = name;
		this.link = link;
		views = 0;
		likes = 0;
		this.date = new Date();
		this.poster = poster;
		this.description = description;
	}
	
	public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
    
    public int getViews() {
    	return views;
    }
    
    public void addView() {
    	views += 1;
    }
    
    public int getLikes() {
    	return likes;
    }
    
    public void setDate(Date date) {
		this.date = date;
	}
	
	public Date getDate(){
		return date;
	}
    
    public void setImage(String image) {
    	this.image = image;
    }
    
    public String getImage() {
    	return image;
    }
    
    public Blob getPoster() {
    	return poster;
    }
    
    public void setPoster(Blob poster) {
    	this.poster = poster;
    }
    
    public List<Comment> getComments(){
    	return comments;
    }
    
}
