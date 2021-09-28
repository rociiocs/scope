package com.example.demo.controllers;

import java.sql.Blob;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.models.Comment;
import com.example.demo.models.Video;
import com.example.demo.repositories.CommentRepository;
import com.example.demo.repositories.VideoRepository;

@Controller
public class CommentController {
	
	@Autowired
	private CommentRepository comments;
	
	@Autowired
	private VideoRepository videos;
	
	@PostMapping("/video/{id}/addComment")
	public String addComment (Model model,@PathVariable long id,@RequestParam String name, 
			@RequestParam String content) throws SQLException {
		
		//Save comment
		Video video = videos.findById(id).orElseThrow();
		Comment comment = new Comment(name,content,video);
		comments.save(comment);
		
		//Model attributes
		model.addAttribute("views",video.getViews());
		model.addAttribute("link", video.getLink());
		model.addAttribute("roomId",id);
		model.addAttribute("name",video.getName());
		Blob foto = video.getPoster();
		if(foto != null) {
			if(foto.length() > 1 ) {
	            byte[] bdata = foto.getBytes(1, (int) foto.length());
	            String s = java.util.Base64.getEncoder().encodeToString(bdata);
	            model.addAttribute("imagen", s);
	        }
		}
		model.addAttribute("comments",video.getComments());
		return "video";
	}
}
