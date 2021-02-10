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

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import com.cos.book.domain.Book;
import com.cos.book.service.BookService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

//단위 테스트 Controller 관련 로직만 (Controller, filter, ControllerAdvice)
//WebMvcTest 내부 - @ExtendWith(SpringExtension.class) = 스프링환경으로 확장함

@Slf4j
@WebMvcTest
public class BookControllerUnitTest {

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean //IoC환경에 bean 등록됨
	private BookService bookService;
	
	//BDDMockito 패턴 given, when, then
	@Test
	public void save_테스트() throws Exception {
		// given 테스트를 하기위한 준비
		// 테스트용 객체만들기 , 자바객체를 JSON으로 매핑하기
		Book book = (new Book( null,"스프링",9.0, 90000));
		String content = new ObjectMapper().writeValueAsString(book);
		
		// 결과 값을 내가 정함!!
		when(bookService.저장하기(book)).thenReturn(new Book( null,"스프링",9.0, 90000));
		
		//when 테스트 실행
		ResultActions resultAction = mockMvc.perform(post("/book")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(content)
				.accept(MediaType.APPLICATION_JSON_UTF8));
				
		//then 검증
		resultAction
		 .andExpect(status().isCreated())
		 .andExpect(jsonPath("$.title").value("스프링"))
		 .andDo(MockMvcResultHandlers.print());
	}
	
	@Test
	public void findAll_테스트() throws Exception{
		//given
		List<Book> books = new ArrayList<>();
		books.add(new Book( null,"스프링",9.0, 90000));
		books.add(new Book( null,"리액트",8.0, 80000));

		// 결과 값을 내가 정함!!
		when(bookService.모두가져오기()).thenReturn(books);
		
		//when
		ResultActions resultAction = mockMvc.perform(get("/book")
				.accept(MediaType.APPLICATION_JSON_UTF8));
		
		//then
		resultAction
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", Matchers.hasSize(2))) //결과값이 2개 일것이다. 라고 내가 썼음
			.andExpect(jsonPath("$.[0].title").value("스프링")) // 0번째거(1L) title이 스프링 일것이다.
			.andDo(MockMvcResultHandlers.print());
	}
	
	@Test
	public void findById_테스트() throws Exception{
		//given
		Long id = 1L;
		when(bookService.한건가져오기(id)).thenReturn(new Book( null,"스프링",9.0, 90000));
		
		//when
		ResultActions resultAction = mockMvc.perform(get("/book/{id}",id)
				.accept(MediaType.APPLICATION_JSON_UTF8));
		
		//then
		resultAction
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.title").value("스프링"))
			.andDo(MockMvcResultHandlers.print());
	}
	
	@Test
	public void update_테스트() throws Exception{
		//given
		Long id = 1L;
		Book book = new Book( null,"스프링",9.0, 90000);
		String content = new ObjectMapper().writeValueAsString(book);
		
		// 결과 값을 내가 정함!!
		when(bookService.수정하기(id, book)).thenReturn(new Book( null,"스프링",9.0, 90000));
		
		//when
		ResultActions resultAction = mockMvc.perform(put("/book/{id}",id)
			.contentType(MediaType.APPLICATION_JSON_UTF8)
			.content(content)
			.accept(MediaType.APPLICATION_JSON_UTF8));
		
		//then
		resultAction
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.title").value("스프링"))
			.andDo(MockMvcResultHandlers.print());
	}
	
	@Test
	public void delete_테스트() throws Exception{
		//given
		Long id = 1L;
		
		// 결과 값을 내가 정함!!
		when(bookService.삭제하기(id)).thenReturn("DeleteOk");
		
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





