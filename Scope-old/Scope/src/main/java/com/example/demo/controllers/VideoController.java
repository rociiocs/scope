package com.example.demo.controllers;

import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.models.Comment;
import com.example.demo.models.Streaming;
import com.example.demo.models.Video;
import com.example.demo.repositories.VideoRepository;


@Controller
public class VideoController {
	
	@Autowired
	private VideoRepository videos;
	
	@PostConstruct
	public void init() {
		//videos.save(new Video("Ensaladilla - Fabian León","https://6140257335b64.streamlock.net/vod/mp4:fabianensaladilla.mp4/playlist.m3u8",null,"Fabian León nos cocina hoy una ensaladilla"));
		//videos.save(new Video("Bizcocho - Fabian León","https://6140257335b64.streamlock.net/vod/mp4:fabianbizcocho.mp4/playlist.m3u8",null,"Fabian León nos cocina hoy un bizcocho de chocolate"));
	}
	
	@GetMapping("/video")
	public String index(Model model) throws SQLException {
		List<String> imagenes = new ArrayList<String>();
		for(Video video: videos.findAll()) {
			Blob foto = video.getPoster();
			if(foto != null) {
				if(foto.length() > 1 ) {
		            byte[] bdata = foto.getBytes(1, (int) foto.length());
		            String s = java.util.Base64.getEncoder().encodeToString(bdata);
		            video.setImage(s);
		        }
			}
		}
		model.addAttribute("videos",videos.findAll());
		return "videos";
	}
	
	@GetMapping("/video/{id}")
	public String showVideo(Model model,@PathVariable long id) throws SQLException {
		Video video = videos.findById(id).orElseThrow();
		video.addView();
		videos.save(video);
		model.addAttribute("views",video.getViews());
		model.addAttribute("link", video.getLink());
		model.addAttribute("roomId",id);
		model.addAttribute("name",video.getName());
		model.addAttribute("description",video.getDescription());
		model.addAttribute("views",video.getViews());
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
	
	@GetMapping("/admin/video")
	public String addVideoForm(Model model){
		return "videoForm";
	}
	
	@PostMapping("admin/video/add")
	public String addStreaming (Model model,@RequestParam String name,
			@RequestParam String url,
			@RequestParam String description,
			@RequestParam MultipartFile image) {
		byte[] bytes;

        if (image != null) {
            try {
                // Por si se quiere guardar tambien el nombre y el tamaño de la imagen
                String nombreFoto = image.getOriginalFilename();
                long tamañoFoto = image.getSize();

                bytes = image.getBytes();

                //String formatName = nombreFoto.substring(nombreFoto.lastIndexOf(".") + 1);
                //bytes = imageServ.resize(bytes, 200, 200, formatName);

                Blob imagen = new javax.sql.rowset.serial.SerialBlob(bytes);

                String bphoto = java.util.Base64.getEncoder().encodeToString(bytes);
                Video video = new Video(name,url,imagen,description);
                videos.save(video);
            }
            catch (Exception exc){
                return "Fallo al establecer la imagen de perfil";
            }
        }
		return "streamingadded";
		
	}
}
