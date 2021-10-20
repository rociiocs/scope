package com.example.demo.controllers;

import java.sql.Blob;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.models.Streaming;
import com.example.demo.models.User;
import com.example.demo.models.Video;
import com.example.demo.repositories.StreamingRepository;
import com.example.demo.repositories.UserRepository;



@Controller
public class StreamingController {

	@Autowired
	private StreamingRepository streamings;
	
	@Autowired
	private UserRepository users;
	
	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");

	@PostConstruct
	public void init() throws ParseException {
		Date start = StringToDate("2021-10-14 09:00:00");
		Date finish = StringToDate("2021-10-15 10:00:00");
		streamings.save(new Streaming("Sala 1","https://cdn3.wowza.com/1/MC85SUU5eFhzSEJ4/NUpXTzcv/hls/live/playlist.m3u8",start,finish,"",1));
		streamings.save(new Streaming("Sala 2","https://6140257335b64.streamlock.net/buck/prueba/playlist.m3u8",start,finish,"",2));
		streamings.save(new Streaming("Scope","https://6140257335b64.streamlock.net/buck/prueba/playlist.m3u8",start,finish,"",3));
		streamings.save(new Streaming("Sala 4","https://6140257335b64.streamlock.net/buck/prueba/playlist.m3u8",start,finish,"",4));
		streamings.save(new Streaming("Sala 5","https://6140257335b64.streamlock.net/buck/prueba/playlist.m3u8",start,finish,"",5));
	}

	@GetMapping("/")
	public String preEvent(Model model,HttpServletRequest request) throws SQLException {
		Streaming streaming = streamings.findById((long) 3).orElseThrow();
		if(new Date().after(streaming.getStart()) && new Date().before(streaming.getEnd())) {
			model.addAttribute("course",true);
			model.addAttribute("date",streaming.getStartString());
			model.addAttribute("start",streaming.getStart());
			model.addAttribute("name",streaming.getName());
			model.addAttribute("id",streaming.getId());
			return "preEvent";
		}else if(new Date().before(streaming.getStart())){
			model.addAttribute("date",streaming.getStartString());
			model.addAttribute("start",streaming.getStart());
			model.addAttribute("name",streaming.getName());
			return "preEvent";
		}else {
			model.addAttribute("name",streaming.getName());
			model.addAttribute("date",streaming.getStartString());
			return "postEvent";
		}
		
	}
	
	@GetMapping("/post")
	public String postEvent(Model model) {
		return "postEvent";
	}
	
	@GetMapping("/streamings")
	public String index(Model model) throws SQLException {
		List<String> imagenes = new ArrayList<String>();
		for(Streaming stream: streamings.findAll()) {
			Blob foto = stream.getPoster();
			if(foto != null) {
				if(foto.length() > 1 ) {
		            byte[] bdata = foto.getBytes(1, (int) foto.length());
		            String s = java.util.Base64.getEncoder().encodeToString(bdata);
		            stream.setImage(s);
		        }
			}
		}
		model.addAttribute("streamings",streamings.findAll());
		return "streamings";
	}
	
	@GetMapping("/admin/streaming")
	public String addStreamingForm(Model model) throws SQLException {
		return "streamingForm";
	}
	
	@GetMapping("/streaming/{id}")
	public String showStreaming(Model model,HttpServletRequest request, @PathVariable long id) throws SQLException {
		Streaming streaming = streamings.findById(id).orElseThrow();
		/*
		if (date.after(streaming.getStart()) && (date.before(streaming.getEnd()))) {
			model.addAttribute("show",true);
		}
		*/
		model.addAttribute("link", streaming.getLink());
		model.addAttribute("roomId",id);
		model.addAttribute("name",streaming.getName());
		model.addAttribute("imagen", null);
		model.addAttribute("description",streaming.getDescription());
		model.addAttribute("date",streaming.getStartString());
		//User
		if(request.getUserPrincipal()!=null) {
			String name = request.getUserPrincipal().getName();
			User user = users.findByName(name).orElseThrow();
			
			if (user != null) {
				model.addAttribute("username",user.getName());
			}
		}
		//Foto
		Blob foto = streaming.getPoster();
		if(foto != null) {
			if(foto.length() > 1 ) {
	            byte[] bdata = foto.getBytes(1, (int) foto.length());
	            String s = java.util.Base64.getEncoder().encodeToString(bdata);
	            model.addAttribute("imagen", s);
	        }
		}
		model.addAttribute("start",streaming.getStart());
		return "streamingEvent";
	}
	
	@PostMapping("admin/streaming/add")
	public String addStreaming (Model model,@RequestParam String name,
			@RequestParam String url, @RequestParam String description,
			@RequestParam MultipartFile image,@RequestParam MultipartFile pdf) {
		Date start = StringToDate("2021-09-04 14:30:00");
		Date finish = StringToDate("2021-09-30 14:40:00");
		byte[] bytes;

        if (image != null && pdf!=null) {
            try {
                // Por si se quiere guardar tambien el nombre y el tamaño de la imagen
                String nombreFoto = image.getOriginalFilename();
                long tamañoFoto = image.getSize();

                bytes = image.getBytes();

                //String formatName = nombreFoto.substring(nombreFoto.lastIndexOf(".") + 1);
                //bytes = imageServ.resize(bytes, 200, 200, formatName);

                Blob imagen = new javax.sql.rowset.serial.SerialBlob(bytes);

                String bphoto = java.util.Base64.getEncoder().encodeToString(bytes);
                
                // Por si se quiere guardar tambien el nombre y el tamaño de la imagen
                String nombrePDF = pdf.getOriginalFilename();
                long tamañoPDF = pdf.getSize();

                bytes = pdf.getBytes();

                //String formatName = nombreFoto.substring(nombreFoto.lastIndexOf(".") + 1);
                //bytes = imageServ.resize(bytes, 200, 200, formatName);

                Blob archivo = new javax.sql.rowset.serial.SerialBlob(bytes);
                
                Streaming streaming = new Streaming(name,url,start,finish,imagen,description,archivo);
                streamings.save(streaming);
            }
            catch (Exception exc){
                return "Fallo al establecer la imagen de perfil";
            }
        }
		return "streamingadded";
		
	}

public Date StringToDate(String s){

    Date result = null;
    try{
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        result  = dateFormat.parse(s);
    }

    catch(ParseException e){
        e.printStackTrace();

    }
    return result ;
}
}
