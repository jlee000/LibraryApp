package com.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.exception.AlreadyExistsException;
import com.exception.ResourceNotFoundException;
import com.model.Author;
import com.model.Book;
import com.model.Image;
import com.repository.BookRepo;

import jakarta.transaction.Transactional;

@Service
public class BookService {
    
    @Autowired
    private BookRepo bookRepo;

    @Autowired
    private AuthorService authorService;

    
	public Book addBook(Book b){
		try {
            if(bookExixts(b.getTitle(),b.getAuthor().getName())) {
                throw new AlreadyExistsException("Book already exists: " + b.getTitle());
            }
            
            Author a = authorService.getAuthorByName(b.getAuthor().getName());
            if(a==null) {
                a = new Author(b.getAuthor().getName(), b.getAuthor().getBiography());
            }
                b.setAuthor(a);
                bookRepo.save(b);			
                return b;
		}catch(AlreadyExistsException e) {
			System.out.println(e);
			return b;
		}
	}
	
	public void deleteBook(Long bookId){
	    bookRepo.findById(bookId)
            .ifPresentOrElse(bookRepo::delete,()->{throw new ResourceNotFoundException("BookId not found");});	
	}
	
	@Transactional
	public Book updateBook(Book b){
		Author a = authorService.getAuthorByName(b.getAuthor().getName());
		if(a==null) {
			a = new Author(b.getAuthor().getName(), b.getAuthor().getBiography());
		}		
		b.setAuthor(a);
		return bookRepo.findById(b.getId())
				.map(book -> b)
				.map(bookRepo::save)
				.orElseThrow(()->new ResourceNotFoundException("BookId not found"));
	}
	
	public List<Book> getBooks(){
		List<Book> books = bookRepo.findAll();
		int i = 0;
		if(books.isEmpty()) { return List.of(); }		
		for(Book b : books) {
			List <Image> images = new ArrayList<>();
			if(b.getImages() != null){
				for(Image image : b.getImages()) {
					images.add(new Image(image.getId(), image.getFilename(), image.getFiletype(), image.getDownloadurl(),b));		
				}
			}
			books.get(i).setImages(images); 
			i+=1;
		}
		return books;
	}
	
	public Book getBookById(Long bookId){
		Optional<Book> book =  bookRepo.findById(bookId);
		Book b = new Book (book.get().getId(), book.get().getTitle(), book.get().getIsbn(), book.get().getDescription(), book.get().getStock(), book.get().getPrice(), book.get().getAuthor(), null);
		List <Image> images = new ArrayList<>();
		if(book.get().getImages() != null){
			for(Image image : book.get().getImages()) {
				images.add(new Image(image.getId(), image.getFilename(), image.getFiletype(), image.getDownloadurl(),b));		
			}
		}
		b.setImages(images);
		return b;
	}

    public Long countBooksByTitleAndAuthorName(String name,String title){		
		return bookRepo.countByTitleAndAuthorName(name,title);
	}

    public Long countBooksByTitle(String title){		
		return bookRepo.countByTitle(title);
	}

    public Long countBooksByAuthorName(String name){		
		return bookRepo.countByAuthorName(name);
	}
	
    public List<Book> getBooksByTitle(String title){
		return bookRepo.findByTitle(title);
	}

    public List<Book> getBooksByAuthorName(String name){
		return bookRepo.findByAuthorName(name);
	}
	
	public boolean checkStock(Book p, int request) {
        int newBookStock = p.getStock()-request;
		return (newBookStock >= 0);
	}
	
	private boolean bookExixts(String title, String author) {
		return bookRepo.existsByTitleAndAuthorName(title,author);
	}    
}
