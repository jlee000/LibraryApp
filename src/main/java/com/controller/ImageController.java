package com.controller;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.config.ApiResponse;
import com.model.Image;
import com.service.ImageService;

import jakarta.transaction.Transactional;

@RestController
public class ImageController {
    
    @Autowired
    private ImageService imageService;
    
	@GetMapping("images/{imageId}")
	@Transactional
	public ResponseEntity<ByteArrayResource> getImageById(@PathVariable Long imageId) throws SQLException{
		Image image = imageService.getImageById(imageId);
		ByteArrayResource resource = new ByteArrayResource(image.getImage().getBytes(1, (int)image.getImage().length()));
		return ResponseEntity.ok()
			.contentType(MediaType.parseMediaType(image.getFiletype()))
			.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + image.getFilename() + "\"")
			.body(resource);
	}
	
	@PostMapping(value="images")
	public ResponseEntity<ApiResponse> addImagesForBook(@RequestBody List<MultipartFile>files, @RequestParam Long bookId){
		try {
		imageService.addImagesForBook(files, bookId);
		return ResponseEntity.ok(new ApiResponse("Add success",bookId));
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Add failed",e));
		}
	}
	
	@PutMapping("images")
	public ResponseEntity<ApiResponse> updateImage(@RequestBody MultipartFile file, @RequestParam Long imageId){
		try {		
		imageService.updateImage(file,imageId);
		return ResponseEntity.ok(new ApiResponse("Upload success",imageId));
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Upload failed",e));
		}
	}
	
	@DeleteMapping("images/{imageId}")
	public ResponseEntity<ApiResponse> deleteImage(@PathVariable Long imageId){
		try {
		imageService.deleteImage(imageId);
		return ResponseEntity.ok(new ApiResponse("Delete success",imageId));
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Delete failed",e));
		}
	}
    
}
