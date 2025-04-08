package com.example.quiz13.entity;

import com.example.quiz13.constants.ResMessage;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "question")
@IdClass(value = QuestionId.class)
public class Question {

	@Min(value = 1, message = ResMessage.ConstantsMessage.PARAM_QUES_ID_ERROR)
	@Id
	@Column(name = "ques_id")
	private int quesId;

	@Id
	@Column(name = "quiz_id")
	private int quizId;

	@NotBlank(message = ResMessage.ConstantsMessage.PARAM_QUIZ_NAME_ERROR)
	@Column(name = "name")
	private String name;

	@NotBlank(message = ResMessage.ConstantsMessage.PARAM_QUES_TYPE_ERROR)
	@Column(name = "type")
	private String type;

	// Boolean 預設 false 不用驗證
	@Column(name = "is_must")
	private boolean must;

	// 因為簡答沒有選項
	@Column(name = "options")
	private String options;

	public Question() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Question( int quizId, int quesId, String name, String type, boolean must, String options) {
		super();
		this.quizId = quizId;
		this.quesId = quesId;
		this.name = name;
		this.type = type;
		this.must = must;
		this.options = options;
	}

	public int getQuesId() {
		return quesId;
	}

	public int getQuizId() {
		return quizId;
	}

	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}

	public boolean isMust() {
		return must;
	}

	public String getOptions() {
		return options;
	}

}
