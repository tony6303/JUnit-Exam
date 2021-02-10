package com.cos.book.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cos.book.domain.Book;
import com.cos.book.domain.BookRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor //final 변수 생성자 자동으로 생성
public class BookService {
	private final BookRepository bookRepository;
	
	@Transactional
	public Book 저장하기(Book book) {
		return bookRepository.save(book);
	}
	
	@Transactional(readOnly = true) // JPA 변경감지 기능이 활성화가 안됨
	//실제 세상에서는 2만원 이지만, 내 트랜잭션 에서는 만원이다.
	public Book 한건가져오기(Long id) {
		return bookRepository.findById(id)
				.orElseThrow(()->new IllegalArgumentException("ID를 확인해주세요") );
		//때릴 함수가 하나밖에없으면 (?) 람다표현식 사용가능 << 다시확인바람
		//Supllier<T> T.get() 고작 이게 끝
	}
	
	@Transactional(readOnly = true)
	public List<Book> 모두가져오기() {
		return bookRepository.findAll();
	}
	
	@Transactional
	public Book 수정하기(Long id, Book book) {
		// 데이터베이스에서 데이터를 가져오면 영속화 되었다. 라고한다.
		Book bookEntity = bookRepository.findById(id)
			.orElseThrow(()->new IllegalArgumentException("ID를 확인해주세요") );
		//영속화 컨텍스트
		
		bookEntity.setTitle(book.getTitle());
		bookEntity.setRating(book.getRating());
		bookEntity.setPrice(book.getPrice());
		
		return bookEntity;
	}
	
	@Transactional
	public String 삭제하기(Long id) {
		bookRepository.deleteById(id); //오류나면 알아서 익셉션 발생
		return "DeleteOk";
	}
	
}
