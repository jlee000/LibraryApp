package com.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.exception.AlreadyExistsException;
import com.exception.ResourceNotFoundException;
import com.model.Author;
import com.repository.AuthorRepo;

@Service
public class AuthorService {
    
    @Autowired
    private AuthorRepo authorRepo;

    public List<Author> getAllAuthors() {
        return authorRepo.findAll();
    }

    public Author getAuthorById(Long authorId) {
        return authorRepo.findById(authorId)
            .orElseThrow(()->new ResourceNotFoundException("Author not found"));
    }

    public Author getAuthorByName(String name) {
        return authorRepo.findByName(name);
    }

    public void addAuthor(String name, String bio) {
		try {
            Author a = getAuthorByName(name);
		
            if(a != null && name.equals(a.getName())){
                throw new AlreadyExistsException("Author already exists");
            }else if(a != null) {
                a.setName(name);
                a.setBiography(bio);
                authorRepo.save(a);
            }else {
                authorRepo.save(new Author(name,bio));
            }		
		}catch(AlreadyExistsException e) {
			System.out.println(e.getMessage());
		}
    }

    public void updateAuthor(Author a) {
        getAuthorById(a.getId());
		authorRepo.save(a);	
    }

    public void deleteAuthor(Long authorId) {
        authorRepo.findById(authorId)
            .ifPresentOrElse(authorRepo::delete,()->{throw new ResourceNotFoundException("AuthorId not found");});	
	 }

    
}
