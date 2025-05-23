package com.example.quiz13.vo;

import java.time.LocalDate;
import java.util.List;

public class FeedbackVo {

	private String userName;

	private String phone;

	private String email;

	private int age;
	
	private List<QuesAnswerVo> quesAnswerList;
	
	private LocalDate fillinDate;

	public FeedbackVo() {
		super();
	}

	public FeedbackVo(String userName, String phone, String email, int age, List<QuesAnswerVo> quesAnswerList,
			LocalDate fillinDate) {
		super();
		this.userName = userName;
		this.phone = phone;
		this.email = email;
		this.age = age;
		this.quesAnswerList = quesAnswerList;
		this.fillinDate = fillinDate;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
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

	public List<QuesAnswerVo> getQuesAnswerList() {
		return quesAnswerList;
	}

	public void setQuesAnswerList(List<QuesAnswerVo> quesAnswerList) {
		this.quesAnswerList = quesAnswerList;
	}

	public LocalDate getFillinDate() {
		return fillinDate;
	}

	public void setFillinDate(LocalDate fillinDate) {
		this.fillinDate = fillinDate;
	}


}
