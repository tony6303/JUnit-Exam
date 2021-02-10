package com.cos.book.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cos.book.domain.Book;
import com.cos.book.service.BookService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor //final 변수 생성자 자동으로 생성
@RestController
public class BookController {
	private final BookService bookService;
	
	@CrossOrigin
	@PostMapping("/book") //RequestBody : HTTP Response Body에 java 객체를 받을 수 있게 한다.
	public ResponseEntity<?> save(@RequestBody Book book){
		return new ResponseEntity<>(bookService.저장하기(book),HttpStatus.CREATED);
	}
	
	@CrossOrigin
	@GetMapping("/book")
	public ResponseEntity<?> findAll(){
		return new ResponseEntity<>(bookService.모두가져오기(),HttpStatus.OK);
	}
	
	@CrossOrigin
	@GetMapping("/book/{id}") // PathVariable : URI의 {id}에 매핑함
	public ResponseEntity<?> findById(@PathVariable Long id){
		return new ResponseEntity<>(bookService.한건가져오기(id),HttpStatus.OK);
	}
	
	@CrossOrigin
	@PutMapping("/book/{id}") 
	public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Book book){
		return new ResponseEntity<>(bookService.수정하기(id, book),HttpStatus.OK);
	}
	
	@CrossOrigin
	@DeleteMapping("/book/{id}") 
	public ResponseEntity<?> DeleteById(@PathVariable Long id){
		return new ResponseEntity<>(bookService.삭제하기(id),HttpStatus.OK);
	}
}
