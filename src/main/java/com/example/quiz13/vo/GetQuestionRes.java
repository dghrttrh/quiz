package com.example.quiz13.vo;

import java.util.List;

import com.example.quiz13.entity.Question;

public class GetQuestionRes extends BasicRes{

	private List<Question> questionList;

	public GetQuestionRes() {
		super();
	}

	public GetQuestionRes(int code, String message) {
		super(code, message);
	}

	public GetQuestionRes(int code, String message, List<Question> questionList) {
		super(code, message);
		this.questionList = questionList;
	}

	public List<Question> getQuestionList() {
		return questionList;
	}

	public void setQuestionList(List<Question> questionList) {
		this.questionList = questionList;
	}
	
	
}
