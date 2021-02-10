package com.cos.book.web;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;

import com.cos.book.domain.Book;
import com.cos.book.domain.BookRepository;
import com.cos.book.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

//MOCK = 모의 웹 서버

//integration : 통합

@Slf4j
@Transactional //테스트함수가 종료될때마다 트랜잭션을 롤백 시킴
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
public class BookControllerIntegreTest {

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private BookRepository bookRepository;
	
	@Autowired
	private EntityManager entityManager;
	
	  //모든 함수가 실행되기전에 이 함수를 먼저 실행함
	@BeforeEach
	public void init() {
		// save와 findAll가 함께 테스트되면 id 가 초기화되지 않아서 1로 초기화시킴.
		//h-2
//		entityManager.createNativeQuery("ALTER TABLE book ALTER COLUMN id RESTART WITH 1").executeUpdate();
		entityManager.createNativeQuery("ALTER TABLE book AUTO_INCREMENT = 1").executeUpdate();
	}
	
	@Test
	public void save_테스트() throws Exception {
		// given 테스트를 하기위한 준비
		// 테스트용 객체만들기 , 자바객체를 JSON으로 매핑하기
		Book book = new Book(  null, "스프링 따라하기", 9.0, 90000);
		String content = new ObjectMapper().writeValueAsString(book);
		
		// 결과 값을 내가 정함!!
		// 단위테스트와 다르게 실제 서비스가 실행되므로 Service가 필요없다.
//		when(bookService.저장하기(book)).thenReturn(new Book(1L,"스프링 따라하기", "코스"));
		
		//when 테스트 실행
		ResultActions resultAction = mockMvc.perform(post("/book")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(content)
				.accept(MediaType.APPLICATION_JSON_UTF8));
				
		//then 검증
		resultAction
		 .andExpect(status().isCreated())
		 .andExpect(jsonPath("$.title").value("스프링 따라하기"))
		 .andDo(MockMvcResultHandlers.print());
	}
	
	@Test
	public void findAll_테스트() throws Exception{
		//given
		List<Book> books = new ArrayList<>();
		books.add(new Book(  null,"스프링",9.0, 90000));
		books.add(new Book(  null,"리액트",8.0, 80000));
		books.add(new Book(  null,"JUnit",7.0 ,70000));
		bookRepository.saveAll(books);
		
		//when
		ResultActions resultAction = mockMvc.perform(get("/book")
				.accept(MediaType.APPLICATION_JSON_UTF8));
		
		//then
		resultAction
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", Matchers.hasSize(3))) //결과값이 3개 일것이다. 라고 내가 썼음
			.andExpect(jsonPath("$.[0].title").value("스프링")) // 0번째거(1L) title이 스프링 일것이다.
			.andDo(MockMvcResultHandlers.print());
	}
	

	@Test
	public void findById_테스트() throws Exception{
		//given
		Long id = 2L;
		List<Book> books = new ArrayList<>();
		books.add(new Book(  null,"스프링",9.0, 90000));
		books.add(new Book(  null,"리액트",8.0, 80000));
		books.add(new Book(  null,"JUnit",7.0, 70000));
		bookRepository.saveAll(books);
		
		//when
		ResultActions resultAction = mockMvc.perform(get("/book/{id}",id)
				.accept(MediaType.APPLICATION_JSON_UTF8));
		
		//then
		resultAction
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.title").value("리액트")) //id = 2L 인 title은 리액트 일것이다.
			.andDo(MockMvcResultHandlers.print());
	}
	
	@Test
	public void update_테스트() throws Exception{
		//given
		Long id = 2L;
		List<Book> books = new ArrayList<>();
		books.add(new Book(  null,"스프링",9.0, 90000));
		books.add(new Book(  null,"리액트",8.0, 80000));
		books.add(new Book(  null,"JUnit",7.0, 70000));
		bookRepository.saveAll(books);
		
		Book book = new Book(  null, "C++",9.0, 90000);
		String content = new ObjectMapper().writeValueAsString(book);
		
//		when(bookService.수정하기(id, book)).thenReturn(new Book(1L,"C++","코스"));
		
		//when
		ResultActions resultAction = mockMvc.perform(put("/book/{id}",id)
			.contentType(MediaType.APPLICATION_JSON_UTF8)
			.content(content)
			.accept(MediaType.APPLICATION_JSON_UTF8));
		
		//then
		resultAction
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.title").value("C++"))
			.andDo(MockMvcResultHandlers.print());
	}
	
	@Test
	public void delete_테스트() throws Exception{
		//given
		Long id = 1L;
		List<Book> books = new ArrayList<>();
		books.add(new Book(  null,"스프링",9.0, 90000));
		books.add(new Book(  null,"리액트",8.0, 80000));
		books.add(new Book(  null,"JUnit",7.0, 70000));
		bookRepository.saveAll(books);
		
		// 결과 값을 내가 정함!!
//		when(bookService.삭제하기(id)).thenReturn("ok");
		
		//when
		ResultActions resultAction = mockMvc.perform(delete("/book/{id}",id)
			.accept(MediaType.TEXT_PLAIN));
		
		//then
		resultAction
			.andExpect(status().isOk())
			.andDo(MockMvcResultHandlers.print());
		
		// 영상보면서 다시 이해하기, 쓰느라 못들음
		MvcResult requestResult = resultAction.andReturn();
		String result = requestResult.getResponse().getContentAsString();
		
		assertEquals("DeleteOk", result);
	}
	
}
