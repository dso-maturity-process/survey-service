/**
 * 
 */
package com.governmentcio.dmp.surveyservice.service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.governmentcio.dmp.dao.DomainFactory;
import com.governmentcio.dmp.dao.QuestionTemplateDao;
import com.governmentcio.dmp.dao.SurveyTemplateDao;
import com.governmentcio.dmp.exception.SurveyServiceException;
import com.governmentcio.dmp.model.QuestionTemplate;
import com.governmentcio.dmp.model.SurveyTemplate;
import com.governmentcio.dmp.repository.QuestionTemplateRepository;
import com.governmentcio.dmp.repository.SurveyTemplateRepository;

/**
 * 
 * @author <a href=mailto:support@governmentcio.com>support</a>
 *
 */
@Service
public class SurveyServiceImpl implements SurveyService {

	/**
	 * Logger instance.
	 */
	private static final Logger LOG = LoggerFactory
			.getLogger(SurveyServiceImpl.class.getName());

	@Autowired
	SurveyTemplateRepository surveyTemplateRepository;

	@Autowired
	QuestionTemplateRepository questionTemplateRepository;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.governmentcio.dmp.surveyservice.controller.SurveyService#
	 * getSurveyTemplates()
	 */
	@Override
	public Iterable<SurveyTemplate> getSurveyTemplates() {

		Iterable<
				SurveyTemplateDao> iterableSurveyTemplateDaos = surveyTemplateRepository
						.findAll();

		Set<SurveyTemplate> surveyTemplates = new HashSet<SurveyTemplate>();

		for (SurveyTemplateDao nextDao : iterableSurveyTemplateDaos) {
			SurveyTemplate surveyTemplate = DomainFactory
					.createSurveyTemplate(nextDao);
			if (null != surveyTemplate) {
				surveyTemplates.add(surveyTemplate);
			}
		}

		return surveyTemplates;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.governmentcio.dmp.surveyservice.controller.SurveyService#
	 * addSurveyTemplate(java.lang.String, java.lang.String)
	 */
	@Override
	@Transactional
	public SurveyTemplate addSurveyTemplate(String name, String description) {

		if (null == name) {
			throw new IllegalArgumentException("Name was null");
		}
		if (null == description) {
			throw new IllegalArgumentException("Description was null");
		}

		LOG.info("Adding survey template [" + name + "]-[" + description + "].");

		SurveyTemplateDao surveyTemplateDao = new SurveyTemplateDao(name,
				description);

		SurveyTemplateDao newSurveyTemplateDao = surveyTemplateRepository
				.save(surveyTemplateDao);

		SurveyTemplate surveyTemplate = DomainFactory
				.createSurveyTemplate(newSurveyTemplateDao);

		LOG.info("Survey template [" + surveyTemplate + "] added.");

		return surveyTemplate;
	}

	/**
	 * 
	 * @param id
	 * @return
	 */
	@Override
	public SurveyTemplate getSurveyTemplateById(final Long id) {
		return DomainFactory
				.createSurveyTemplate(surveyTemplateRepository.findById(id).get());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.governmentcio.dmp.surveyservice.controller.SurveyService#
	 * getSurveyTemplateByName(java.lang.String)
	 */
	@Override
	public SurveyTemplate getSurveyTemplateByName(String name) {

		if ((name == null) || (name.length() == 0)) {
			throw new IllegalArgumentException("Template name was null or empty");
		}

		return DomainFactory
				.createSurveyTemplate(surveyTemplateRepository.findByName(name));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.governmentcio.dmp.surveyservice.controller.SurveyService#
	 * updateSurveyTemplate(com.governmentcio.dmp.model.SurveyTemplate)
	 */
	@Override
	@Transactional
	public void updateSurveyTemplate(SurveyTemplate surveyTemplate)
			throws SurveyServiceException {

		if (null == surveyTemplate) {
			throw new IllegalArgumentException("SurveyTemplate parameter was null");
		}

		Optional<SurveyTemplateDao> retrievedST = surveyTemplateRepository
				.findById(surveyTemplate.getId());

		if (!retrievedST.isPresent()) {
			throw new SurveyServiceException("Survey template ["
					+ surveyTemplate.getName() + "] not found for update.");
		}

		retrievedST.get().setName(surveyTemplate.getName());
		retrievedST.get().setDescription(surveyTemplate.getDescription());

		surveyTemplateRepository.save(retrievedST.get());

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.governmentcio.dmp.surveyservice.controller.SurveyService#
	 * removeSurveyTemplate(java.lang.String)
	 */
	@Override
	public void removeSurveyTemplate(String name) {

		if ((name == null) || (name.length() == 0)) {
			throw new IllegalArgumentException(
					"User name parameter was null or empty");
		}

		SurveyTemplateDao surveyTemplateDao = surveyTemplateRepository
				.findByName(name);

		if (null != surveyTemplateDao) {
			surveyTemplateRepository.delete(surveyTemplateDao);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.governmentcio.dmp.surveyservice.controller.SurveyService#
	 * getQuestionTemplates()
	 */
	@Override
	public Iterable<QuestionTemplate> getQuestionTemplates() {

		Iterable<QuestionTemplateDao> iterableUserDaos = questionTemplateRepository
				.findAll();

		Set<QuestionTemplate> iterableQuestions = new HashSet<QuestionTemplate>();

		for (QuestionTemplateDao nextQuestionTemplateDao : iterableUserDaos) {
			QuestionTemplate questionTemplate = DomainFactory
					.createQuestionTemplate(nextQuestionTemplateDao);
			if (null != questionTemplate) {
				iterableQuestions.add(questionTemplate);
			}
		}

		return iterableQuestions;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.governmentcio.dmp.surveyservice.controller.SurveyService#
	 * addQuestionTemplate(java.lang.String)
	 */
	@Override
	@Transactional
	public QuestionTemplate addQuestionTemplate(final String text) {

		if (null == text) {
			throw new IllegalArgumentException("Text was null");
		}

		LOG.info("Adding question template [" + text + "].");

		QuestionTemplate question = DomainFactory.createQuestionTemplate(
				questionTemplateRepository.save(new QuestionTemplateDao(text)));

		LOG.info("Question template [" + question + "] added.");

		return question;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.governmentcio.dmp.surveyservice.controller.SurveyService#
	 * getQuestionTemplateById(java.lang.Long)
	 */
	@Override
	public QuestionTemplate getQuestionTemplateById(Long id)
			throws SurveyServiceException {

		Optional<QuestionTemplateDao> questionOptional = questionTemplateRepository
				.findById(id);

		QuestionTemplate questionTemplate = null;

		if (questionOptional.isPresent()) {
			questionTemplate = DomainFactory.createQuestionTemplate(
					questionTemplateRepository.findById(id).get());
		}

		return questionTemplate;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.governmentcio.dmp.surveyservice.controller.SurveyService#
	 * updateTemplateQuestion(com.governmentcio.dmp.model.QuestionTemplate)
	 */
	@Override
	@Transactional
	public void updateTemplateQuestion(final QuestionTemplate questionTemplate)
			throws SurveyServiceException {

		if (null == questionTemplate) {
			throw new IllegalArgumentException("Question was null");
		}

		Optional<
				QuestionTemplateDao> retrievedQuestionDao = questionTemplateRepository
						.findById(questionTemplate.getId());

		if (!retrievedQuestionDao.isPresent()) {
			throw new SurveyServiceException("Question [" + questionTemplate.getText()
					+ "] not found for update.");
		}

		retrievedQuestionDao.get().setText(questionTemplate.getText());

		questionTemplateRepository.save(retrievedQuestionDao.get());

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.governmentcio.dmp.surveyservice.controller.SurveyService#
	 * removeQuestionTemplate(java.lang.Long)
	 */
	@Override
	public void removeQuestionTemplate(Long id) throws SurveyServiceException {

		Optional<
				QuestionTemplateDao> retrievedQuestionDao = questionTemplateRepository
						.findById(id);

		if (!retrievedQuestionDao.isPresent()) {
			throw new SurveyServiceException(
					"Question not found for update using id [" + id + "]");
		}

		questionTemplateRepository.delete(retrievedQuestionDao.get());

	}

}
