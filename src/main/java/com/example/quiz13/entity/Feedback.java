package com.example.quiz13.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

@Entity
@Table(name = "feedback")
@IdClass(value = FeedbackId.class)
public class Feedback {

	@Id
	@Column(name = "quiz_id")
	private int quizId;
	
	@Id
	@Column(name = "ques_id")
	private int quesId;
	
	@Column(name = "user_name")
	private String userName;
	
	@Column(name = "phone")
	private String phone;
	
	@Id
	@Column(name = "email")
	private String email;
	
	@Column(name = "age")
	private int age;
	
	@Column(name = "answers")
	private String answers;
	
	@Column(name = "fillin_date")
	private LocalDate fillinDate;

	public Feedback() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Feedback(int quizId, int quesId, String userName, String phone, String email, int age, String answers,
			LocalDate fillinDate) {
		super();
		this.quizId = quizId;
		this.quesId = quesId;
		this.userName = userName;
		this.phone = phone;
		this.email = email;
		this.age = age;
		this.answers = answers;
		this.fillinDate = fillinDate;
	}

	public int getQuizId() {
		return quizId;
	}

	public void setQuizId(int quizId) {
		this.quizId = quizId;
	}

	public int getQuesId() {
		return quesId;
	}

	public void setQuesId(int quesId) {
		this.quesId = quesId;
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

	public String getAnswers() {
		return answers;
	}

	public void setAnswers(String answers) {
		this.answers = answers;
	}

	public LocalDate getFillinDate() {
		return fillinDate;
	}

	public void setFillinDate(LocalDate fillinDate) {
		this.fillinDate = fillinDate;
	}
	
	
}
