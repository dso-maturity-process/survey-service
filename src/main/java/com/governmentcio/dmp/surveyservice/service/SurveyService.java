package com.governmentcio.dmp.surveyservice.service;

import com.governmentcio.dmp.exception.SurveyServiceException;
import com.governmentcio.dmp.model.QuestionTemplate;
import com.governmentcio.dmp.model.SurveyTemplate;

public interface SurveyService {

	/**
	 * 
	 * @return
	 */
	Iterable<SurveyTemplate> getSurveyTemplates();

	/**
	 * 
	 * @param user
	 * @throws Exception
	 */
	SurveyTemplate addSurveyTemplate(String name, String description);

	/**
	 * 
	 * @param name
	 * @return
	 */
	SurveyTemplate getSurveyTemplateByName(String name);

	/**
	 * 
	 * @param id
	 * @return
	 */
	SurveyTemplate getSurveyTemplateById(Long id);

	/**
	 * 
	 * @param user
	 * @throws Exception
	 */
	void updateSurveyTemplate(SurveyTemplate surveyTemplate)
			throws SurveyServiceException;

	/**
	 * 
	 * @param name
	 */
	void removeSurveyTemplate(String name);

	/**
	 * 
	 * @return
	 */
	Iterable<QuestionTemplate> getQuestionTemplates();

	/**
	 * 
	 * @param user
	 * @throws Exception
	 */
	QuestionTemplate addQuestionTemplate(String text);

	/**
	 * 
	 * @param id
	 * @return
	 */
	QuestionTemplate getQuestionTemplateById(Long id)
			throws SurveyServiceException;

	/**
	 * 
	 * @param questionTemplate
	 * @throws SurveyServiceException
	 */
	void updateTemplateQuestion(QuestionTemplate questionTemplate)
			throws SurveyServiceException;

	/**
	 * 
	 * @param id
	 */
	void removeQuestionTemplate(Long id) throws SurveyServiceException;

	/**
	 * 
	 * @param questionTemplateId
	 * @param surveyTemplateId
	 * @param sequence
	 * @throws SurveyServiceException
	 */
	void addQuestionTemplateToSurveyTemplate(final Long questionTemplateId,
			final Long surveyTemplateId, final Long sequence)
			throws SurveyServiceException;

}