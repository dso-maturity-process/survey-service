/**
 * 
 */
package com.governmentcio.dmp.surveyservice.controller;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
@RestController
@RequestMapping("/survey")
public class SurveyServiceController {

	/**
	 * Logger instance.
	 */
	private static final Logger LOG = LoggerFactory
			.getLogger(SurveyServiceController.class.getName());

	@Autowired
	SurveyTemplateRepository surveyTemplateRepository;

	@Autowired
	QuestionTemplateRepository questionTemplateRepository;

	/**
	 * 
	 * @return
	 */
	@GetMapping("/allSurveyTemplates")
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

	/**
	 * 
	 * @param user
	 * @throws Exception
	 */
	@Transactional
	@RequestMapping(value = "/addSurveyTemplate", method = RequestMethod.POST)
	public SurveyTemplate addSurveyTemplate(
			@RequestParam(value = "name", required = true) String name,
			@RequestParam(value = "description", required = true) String description) {

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
	 * @param name
	 * @return
	 */
	@GetMapping("/getSurveyTemplate/{name}")
	public SurveyTemplate getSurveyTemplateByName(@PathVariable String name) {

		if ((name == null) || (name.length() == 0)) {
			throw new IllegalArgumentException("Template name was null or empty");
		}

		return DomainFactory
				.createSurveyTemplate(surveyTemplateRepository.findByName(name));
	}

	/**
	 * 
	 * @param user
	 * @throws Exception
	 */
	@Transactional
	@RequestMapping(value = "/updateSurveyTemplate", method = RequestMethod.POST)
	public void updateSurveyTemplate(
			@RequestBody(required = true) SurveyTemplate surveyTemplate)
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

	/**
	 * 
	 * @param name
	 */
	@DeleteMapping("/removeSurveyTemplate/{name}")
	public void removeSurveyTemplate(@PathVariable String name) {

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

	/**
	 * 
	 * @return
	 */
	@GetMapping("/allQuestionTemplates")
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

	/**
	 * 
	 * @param user
	 * @throws Exception
	 */
	@Transactional
	@RequestMapping(value = "/addQuestionTemplate", method = RequestMethod.POST)
	public QuestionTemplate addQuestionTemplate(
			@RequestParam(value = "text", required = true) String text) {

		if (null == text) {
			throw new IllegalArgumentException("Text was null");
		}

		LOG.info("Adding question template [" + text + "].");

		QuestionTemplate question = DomainFactory.createQuestionTemplate(
				questionTemplateRepository.save(new QuestionTemplateDao(text)));

		LOG.info("Question template [" + question + "] added.");

		return question;
	}

	/**
	 * 
	 * @param id
	 * @return
	 */
	@GetMapping("/getQuestionTemplate/{id}")
	public QuestionTemplate getQuestionTemplateById(@PathVariable Long id)
			throws SurveyServiceException {

		Optional<QuestionTemplateDao> questionOptional = questionTemplateRepository
				.findById(id);

		QuestionTemplate questionTemplate = null;

		if (questionOptional.isPresent()) {
			questionTemplate = DomainFactory
					.createQuestionTemplate(questionTemplateRepository.findById(id).get());
		}

		return questionTemplate;

	}

	/**
	 * 
	 * @param questionTemplate
	 * @throws SurveyServiceException
	 */
	@Transactional
	@RequestMapping(value = "/updateQuestionTemplate", method = RequestMethod.POST)
	public void updateTemplateQuestion(
			@RequestBody(required = true) QuestionTemplate questionTemplate)
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

	/**
	 * 
	 * @param id
	 */
	@DeleteMapping("/removeQuestionTemplate/{id}")
	public void removeQuestionTemplate(@PathVariable Long id)
			throws SurveyServiceException {

		Optional<
				QuestionTemplateDao> retrievedQuestionDao = questionTemplateRepository
						.findById(id);

		if (!retrievedQuestionDao.isPresent()) {
			throw new SurveyServiceException(
					"Question not found for update usind id [" + id + "]");
		}

		questionTemplateRepository.delete(retrievedQuestionDao.get());

	}

	/**
	 * 
	 * @param name
	 * @return
	 */
	@PostMapping("/healthz")
	public boolean healthz() {

		LOG.info("Checking health...");

		LOG.info("Survey service is healthy");

		return true; // TODO: Replace canned response.
	}

}
