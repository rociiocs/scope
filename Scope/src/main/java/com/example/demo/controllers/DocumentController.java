package com.example.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import com.example.demo.models.Streaming;
import com.example.demo.repositories.StreamingRepository;


import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Optional;

@RestController
public class DocumentController {
	@Autowired
	private StreamingRepository streamings;
	
	@RequestMapping(value = "downloadFile", method = RequestMethod.GET)
    public StreamingResponseBody getStreamingFile(HttpServletResponse response) throws IOException, SQLException {
	
        Streaming streaming = streamings.findById((long) 0).orElseThrow();
        Blob blob = streaming.getDocument();
        InputStream inputStream = blob.getBinaryStream();
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=\"schedule.pdf\"");
        return outputStream -> {
            int nRead;
            byte[] data = new byte[1024];
            while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
                System.out.println("Writing some bytes..");
                outputStream.write(data, 0, nRead);
            }
        };
        
	}
}
