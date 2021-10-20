package com.example.demo.models;

import java.sql.Blob;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Streaming {
	@Id
	private long id;
	private String name;
	private String link;
	private String description;
	private int views;
	private int likes;
	private Date start;
	private Date end;
	@Lob
	@JsonIgnore
	private Blob poster;
	private String image;
	@Lob
	@JsonIgnore
	private Blob document;
	public Streaming() {
		
	}
	public Streaming(String name,String link,Date start,Date end,String description,long id) {
		this.name = name;
		this.link = link;
		this.start = start;
		this.end = end;
		this.description = description;
		views = 0;
		likes = 0;
		this.id = id;
	}
	public Streaming(String name,String link,Date start,Date end,Blob poster,String description,Blob document) {
		this.name = name;
		this.link = link;
		this.start = start;
		this.end = end;
		this.description = description;
		views = 0;
		likes = 0;
		this.poster = poster;
		this.document = document;
	}
	
	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
    
    public int getLikes() {
    	return likes;
    }
    
    public void addLike(){
    	likes +=1;
    }
    
    public void setStart(Date start) {
    	this.start = start;
    }
    public Date getStart() {
    	return start;
    }
    
    public void setEnd() {
    	this.end = end;
    }
    
    public void setImage(String image) {
    	this.image = image;
    }
    
    public String getImage() {
    	return image;
    }
    public Date getEnd() {
    	return end;
    }
    
    public Blob getPoster() {
    	return poster;
    }
    
    public void setPoster(Blob poster) {
    	this.poster = poster;
    }
    
    public Blob getDocument() {
    	return document;
    }
    
    public void setDocument(Blob document) {
    	this.document = document;
    }
    
    public String getStartString() {
    	String pattern = "EEEEE d MMMMM yyyy HH:mm";
    	SimpleDateFormat simpleDateFormat =new SimpleDateFormat(pattern, new Locale("es", "ES"));
    	String date = simpleDateFormat.format(start);
    	return date;
    }
}
