package com.example.quiz13.vo;

import java.util.List;

import com.example.quiz13.constants.ResMessage;

import jakarta.validation.constraints.NotEmpty;

public class DeleteReq {
	
	@NotEmpty(message = ResMessage.ConstantsMessage.PARAM_QUIZ_ID_LIST_ERROR)
	private List<Integer> quiaIdList;

	public List<Integer> getQuiaIdList() {
		return quiaIdList;
	}

	public void setQuiaIdList(List<Integer> quiaIdList) {
		this.quiaIdList = quiaIdList;
	}
	
	
}
