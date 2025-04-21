package com.example.quiz13;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.quiz13.dao.QuizDao;
import com.example.quiz13.entity.Question;
import com.example.quiz13.service.ifs.QuizService;
import com.example.quiz13.service.impl.QuizServiceImpl;
import com.example.quiz13.vo.BasicRes;
import com.example.quiz13.vo.CreateReq;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
class Quiz13ApplicationTests {

	@Autowired
	private QuizDao quizDao;
	
	@Autowired
	private QuizService quiz;
	
	@Test
	void contextLoads() throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		String str = mapper.writeValueAsString(List.of("AAA", "BBB", "CCC"));
		String str1 = mapper.writeValueAsString(List.of("AAA", "BBB", "CCC"));
		List<Question> list = new ArrayList<>();
		list.add(new Question(4,3,"A","text",true, null));
//		list.add(new Question(2,3,"A","single",true, str));
//		list.add(new Question(3,3,"A","single",true, str));
		LocalDate date = LocalDate.of(2025, 3, 27);
		LocalDate endDate = LocalDate.of(2025, 3, 28);
		CreateReq req = new CreateReq("A03","B",date,endDate,true,list);
		BasicRes res = quiz.create(req);
		System.out.println(res.getCode() + res.getMessage());
	}
	
	@Test
	public void test() {
		
	}
	
}
