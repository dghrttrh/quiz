package com.example.quiz13.vo;

import java.util.List;

import com.example.quiz13.constants.ResMessage;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class FillinReq {

	@Min(value = 1, message = ResMessage.ConstantsMessage.PARAM_QUIZ_ID_ERROR)
	private int quizId;
	
	private String name;
	
	private String phone;
	
	@NotBlank(message = ResMessage.ConstantsMessage.EMAIL_IS_NECESSARY)
	private String email;
	
	private int age;
	
	// 此參數不用驗證，一個極端的情況是所有問題都是簡答且是非必填的
	private List<QuesIdAnswerVo> answerList;

	public int getQuizId() {
		return quizId;
	}

	public void setQuizId(int quizId) {
		this.quizId = quizId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public List<QuesIdAnswerVo> getAnswerList() {
		return answerList;
	}

	public void setAnswerList(List<QuesIdAnswerVo> answerList) {
		this.answerList = answerList;
	}
	
	
}
