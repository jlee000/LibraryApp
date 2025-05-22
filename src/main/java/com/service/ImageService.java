package com.service;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.sql.rowset.serial.SerialBlob;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.exception.ResourceNotFoundException;
import com.model.Book;
import com.model.Image;
import com.repository.ImageRepo;

@Service
public class ImageService {
    
    @Autowired
    private ImageRepo imageRepo;

    @Autowired
    private BookService bookService;
	
	public Image getImageById(Long imageId) {
		return imageRepo.findById(imageId).orElseThrow(()->new ResourceNotFoundException("ImageId not found" + imageId));
	}
	
	public void addImagesForBook (List<MultipartFile> files, Long bookId) {
		Book b = bookService.getBookById(bookId);
		for(MultipartFile file : files) {
			try {
				Image image = new Image();
				image.setFilename(file.getOriginalFilename());
				image.setFiletype(file.getContentType());
				image.setImage(new SerialBlob(file.getBytes()));
				image.setBook(b);
				Image savedImage = imageRepo.save(image);
				savedImage.setDownloadurl("http://localhost:8080/images/"+savedImage.getId());
				imageRepo.save(savedImage);
				
			}catch(IOException | SQLException e) {
				throw new RuntimeException(e.getMessage());
			}
		}		
	}
	
	public void updateImage(MultipartFile file, Long imageId) {
		try {
			Image image = getImageById(imageId);
			image.setFilename(file.getOriginalFilename());
			image.setFiletype(file.getContentType());
			image.setImage(new SerialBlob(file.getBytes()));
			imageRepo.save(image);	
		}catch(IOException | SQLException e) {
			throw new RuntimeException(e.getMessage());
		}
	}
	
	public void deleteImage(Long imageId) {	
		imageRepo.findById(imageId).
            ifPresentOrElse(imageRepo::delete,()->{throw new ResourceNotFoundException("ImageId not found");});	
	}
	
    
}
