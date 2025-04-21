package com.example.quiz13.service.impl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.example.quiz13.constants.QuesType;
import com.example.quiz13.constants.ResMessage;
import com.example.quiz13.dao.QuestionDao;
import com.example.quiz13.dao.QuizDao;
import com.example.quiz13.entity.Question;
import com.example.quiz13.entity.Quiz;
import com.example.quiz13.service.ifs.QuizService;
import com.example.quiz13.vo.BasicRes;
import com.example.quiz13.vo.CreateReq;
import com.example.quiz13.vo.DeleteReq;
import com.example.quiz13.vo.GetQuestionRes;
import com.example.quiz13.vo.SearcgReq;
import com.example.quiz13.vo.SearchRes;
import com.example.quiz13.vo.UpdateReq;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.transaction.Transactional;

@Service
public class QuizServiceImpl implements QuizService {

	@Autowired
	private QuizDao quizDao;

	@Autowired
	private QuestionDao questionDao;

	// 清除暫存資料: 只有 cacheNames 沒有 key，會把 cacheNames 是 quiz_search 的所有暫存資料清除
	// 如果是 cacheNames + key，則是只清除特定的暫存資料； key 的參數值一樣使用 #p0 來表示，但通常不會只清除特定資料
	// allEntries: 強制刪除指定的 cacheNames 底下所有 key 對應的暫存資料，預設是 false
	@CacheEvict(cacheNames = "get_search", allEntries = true)
	// @Transactional 只有錯誤發生在 RuntimeException(或其他子類別)時才會將資料回朔
	// rollbackOn(jakarta庫) = Exception.class 是將資料回朔指定到只要發生 Exception 時，就會將資料回朔
	// 因為 Exception 是所有 XXXException 的父類別，只要是發生 XXXException 時，資料回朔都會有效
	// : rollbackFor(org.springframework庫)
	@Transactional(rollbackOn = Exception.class)
	@Override
	public BasicRes create(CreateReq req) {
		// 參數檢查: req的參數已透過 @Valid 做基本的檢查
		// 開始時間不能比結束時間晚
		if (req.getStartDate().isAfter(req.getEndDate())) {
			return new BasicRes(ResMessage.PARAM_DATE_ERROR.getCode(), //
					ResMessage.PARAM_DATE_ERROR.getMessage());
		}
		List<Question> quesList = req.getQuestionList();
		// 檢查 question type
		BasicRes checkRes = checkQuestions(quesList);
		if (checkRes != null) {
			return checkRes;
		}
		try {
			// 將資料寫進 DB
			quizDao.insert(req.getName(), req.getDescription(), req.getStartDate(), req.getEndDate(),
					req.isPublished());
			// 取得最新一筆資料的 quiz_id
			int quizId = quizDao.selectMaxQuizId();
			// 將資料寫進 question
			for (Question item : quesList) {
				questionDao.insertQuestion(//
						quizId, //
						item.getQuesId(), //
						item.getName(), //
						item.getType(), //
						item.isMust(), //
						item.getOptions());
			}
		} catch (Exception e) {
			// 錯誤要拋(throw) 出去，@Transactional 才會生效(將資料回朔)
			throw e;
		}
		return new BasicRes(ResMessage.SUCCESS.getCode(), //
				ResMessage.SUCCESS.getMessage());
	}

	private BasicRes checkQuestions(List<Question> list) {
		for (Question item : list) {
			// 檢查 question type
			if (!QuesType.checkType(item.getType())) {
				return new BasicRes(ResMessage.QUES_TYPE_ERROR.getCode(), //
						ResMessage.QUES_TYPE_ERROR.getMessage());
			}

			// 排除: type 不是 text(是單選或多選時)
			if (!item.getType().equalsIgnoreCase(QuesType.TEXT.getType())//
					&& !StringUtils.hasText(item.getOptions())) {
				return new BasicRes(ResMessage.PARAM_OPTIONS_ERROR.getCode(), //
						ResMessage.PARAM_OPTIONS_ERROR.getMessage());
			}
			// 排除: type 是 text時，選項有值
			if (item.getType().equalsIgnoreCase(QuesType.TEXT.getType())//
					&& StringUtils.hasText(item.getOptions())) {
				return new BasicRes(ResMessage.PARAM_OPTIONS_ERROR.getCode(), //
						ResMessage.PARAM_OPTIONS_ERROR.getMessage());
			}
			// 問卷的 type = text ，options 沒資料，所以不用執行後續的選項個數檢查
			if (item.getType().equalsIgnoreCase(QuesType.TEXT.getType())//
					&& !StringUtils.hasText(item.getOptions())) {
				return null;
			}

			// 檢查複選題選項至少要有 2 個
			// 將選項字串轉成 List<String>: 要先確定當初創建問卷時，前端的多個選項是陣列，且使用 Stringify 轉成字串型態
			// 前端選項原本格式(陣列): ["aa","bb", "cc"]
			ObjectMapper mapper = new ObjectMapper();
			try {
				List<String> optionsList = mapper.readValue(item.getOptions(), new TypeReference<>() {
				});
				if (item.getType().equalsIgnoreCase(QuesType.SINGLE.getType())//
						|| item.getType().equalsIgnoreCase(QuesType.MULTI.getType())) {
					if (optionsList.size() < 2) {
						return new BasicRes(ResMessage.OPTIONS_SIZE_ERROR.getCode(), //
								ResMessage.OPTIONS_SIZE_ERROR.getMessage());
					}
				}
			} catch (Exception e) {
				return new BasicRes(ResMessage.OPTIONS_PARSE_ERROR.getCode(), //
						ResMessage.OPTIONS_PARSE_ERROR.getMessage());
			}
		}
		return null;
	}

	@Override
	public SearchRes getAll() {
		List<Quiz> list = quizDao.getAll();
		return new SearchRes(ResMessage.SUCCESS.getCode(), //
				ResMessage.SUCCESS.getMessage(), //
				list);
	}

	@Override
	public GetQuestionRes getQuestionByQuizid(int quizId) {
		if (quizId <= 0) {
			return new GetQuestionRes(ResMessage.PARAM_QUIZ_ID_ERROR.getCode(), //
					ResMessage.PARAM_QUIZ_ID_ERROR.getMessage());
		}
		return new GetQuestionRes(ResMessage.SUCCESS.getCode(), //
				ResMessage.SUCCESS.getMessage(), //
				questionDao.getByQuizId(quizId));
	}

	// 因為 update 的方法包含了跨表操作，所以要將這些 Dao 的操作當成同一次的行為
	@Transactional(rollbackOn = Exception.class)
	@Override
	public BasicRes update(UpdateReq req) {
		// 要檢查 quiz 的 id 是否存在，因為最後是先將問題的資料刪除後再新增，而不是用更新語法
		// 不使用更新語法的考量原因是，更新前有5題，更新後變成4題，若是用更新語法，原本的第5題還會存在
		// 因 quiz 的 id 不存在，問題刪除時資料不會變更，但接著新增資料時就會把增加資料，且這些資料沒有對應的 quiz_id

		int count = quizDao.selectById(req.getId());
		if (count != 1) {
			return new BasicRes(ResMessage.ID_NOT_FOUND.getCode(), //
					ResMessage.ID_NOT_FOUND.getMessage());
		}
		// 開始時間不能比結束時間晚
		if (req.getStartDate().isAfter(req.getEndDate())) {
			return new BasicRes(ResMessage.PARAM_DATE_ERROR.getCode(), //
					ResMessage.PARAM_DATE_ERROR.getMessage());
		}
		List<Question> quesList = req.getQuestionList();
		// 檢查 question type
		BasicRes checkRes = checkQuestions(quesList);
		if (checkRes != null) {
			return checkRes;
		}
		int quizId = req.getId();
		// 檢查 Question 中的 quizId 是否與 Quiz 的 id 值一樣
		for (Question item : req.getQuestionList()) {
			if (item.getQuizId() != quizId) {
				return new BasicRes(ResMessage.QUIZ_ID_MISMATCH.getCode(), //
						ResMessage.QUIZ_ID_MISMATCH.getMessage());
			}
		}
		try {
			// 1.更新問卷
			quizDao.updateById(quizId, req.getName(), req.getDescription(), //
					req.getStartDate(), req.getEndDate(), req.isPublished());
			// 2.刪除同張問卷的所有問題
			questionDao.deleteByQuizId(quizId);
			;
			// 3.新增問題
			for (Question item : quesList) {
				questionDao.insertQuestion(//
						quizId, //
						item.getQuesId(), //
						item.getName(), //
						item.getType(), //
						item.isMust(), //
						item.getOptions());
			}
		} catch (Exception e) {
			throw e;
		}

		return new BasicRes(ResMessage.SUCCESS.getCode(), //
				ResMessage.SUCCESS.getMessage());
	}

	/**
	 * 1. cacheNames 可以當成是書本目錄的"章"，key 可以當成是書本目錄的"節"<br>
	 * 2. cacheNames 等號後面的字串名稱自定義，可以多個，多個時要使用大括號{}，例如: cacheNames = {"A", "B"}<br>
	 * 3. key 等號後面的字串，因為 req 是物件，使用 #req 會取不到參數值，
	 *     簡單點的方法是使用位置 #p0 來表示方法中第一個參數<br>
	 * 4. Cache 不支援 concat("字串1", "字串2", "字串3") 這種寫法<br>
	 *    4.1 多參數的串接，不使用 concat，直接在字串中使用加號(+)串多個參數，字串中使用單引號來表示字串<br>
	 *    4.2 concat 中非字串參數值要使用方法 toString() 轉成字串<br>
	 * 5. unless 可以翻成排除的意思，後面的字串是指會排除符合條件的 --> 排除 res 不成功，即只暫存成功時的資料<br>
	 * 6. #result: 表示方法返回的結果；即使是不同方法有不同的返回資料型態，也通用<br>
	 */
	@Cacheable(cacheNames = "get_search", //
			key = "#p0.quizName + '-' + #p0.startDate.toString() + '-' + #p0.endDate.toString()",//
			unless="#result.code != 200")
	@Override
	public SearchRes getAll(SearcgReq req) {
		List<Quiz> list = quizDao.getAll(req.getQuizName(), req.getStartDate(), req.getEndDate());

		return new SearchRes(//
				ResMessage.SUCCESS.getCode(), //
				ResMessage.SUCCESS.getMessage(), //
				list);
	}

	@Transactional(rollbackOn = Exception.class)
	@Override
	public BasicRes delete(DeleteReq req) {
		try {
			quizDao.delete(req.getQuiaIdList());
			questionDao.delete(req.getQuiaIdList());
		} catch (Exception e) {
			throw e;
		}
		return new BasicRes(//
				ResMessage.SUCCESS.getCode(), //
				ResMessage.SUCCESS.getMessage());
	}

}
