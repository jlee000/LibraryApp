package com.model;
import java.sql.Blob;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;

@Entity
public class Image {

	@Id	
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String filename;
	
	private String filetype;
	
	@Lob
	private Blob image;
	
	private String downloadurl;
	
	@ManyToOne
	@JoinColumn(name="book_id")
    @JsonIgnore
	private Book book;
    
    public Image(Long id, String filename, String filetype, Blob image, String downloadurl, Book book) {
        this.id = id;
        this.filename = filename;
        this.filetype = filetype;
        this.image = image;
        this.downloadurl = downloadurl;
        this.book = book;
    }

    public Image(Long id, String filename, String filetype, String downloadurl, Book book) {
		this.id = id;
		this.filename = filename;
		this.filetype = filetype;
		this.downloadurl = downloadurl;
        this.book = book;
	}

    public Image() {
    }
    
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getFilename() {
        return filename;
    }
    public void setFilename(String filename) {
        this.filename = filename;
    }
    public String getFiletype() {
        return filetype;
    }
    public void setFiletype(String filetype) {
        this.filetype = filetype;
    }
    public Blob getImage() {
        return image;
    }
    public void setImage(Blob image) {
        this.image = image;
    }
    public String getDownloadurl() {
        return downloadurl;
    }
    public void setDownloadurl(String downloadurl) {
        this.downloadurl = downloadurl;
    }
    public Book getBook() {
        return book;
    }
    public void setBook(Book book) {
        this.book = book;
    }

    @Override
    public String toString() {
        return "Image [id=" + id + ", filename=" + filename + ", filetype=" + filetype + ", image=" + image
                + ", downloadurl=" + downloadurl + ", book=" + book + "]";
    }    
}
