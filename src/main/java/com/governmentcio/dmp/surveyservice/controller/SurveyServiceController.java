/**
 * 
 */
package com.governmentcio.dmp.surveyservice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.governmentcio.dmp.exception.SurveyServiceException;
import com.governmentcio.dmp.model.QuestionTemplate;
import com.governmentcio.dmp.model.SurveyTemplate;
import com.governmentcio.dmp.surveyservice.service.SurveyService;
import com.governmentcio.dmp.utility.ServiceHealth;

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

	SurveyService surveyService;

	/**
	 * @return the surveyService
	 */
	public SurveyService getSurveyService() {
		return surveyService;
	}

	/**
	 * @param surveyService the surveyService to set
	 */
	@Autowired
	public void setSurveyService(SurveyService surveyService) {
		this.surveyService = surveyService;
	}

	/**
	 * 
	 * @return
	 */
	@GetMapping("/allSurveyTemplates")
	public Iterable<SurveyTemplate> getSurveyTemplates() {
		return surveyService.getSurveyTemplates();
	}

	/**
	 * 
	 * @param user
	 * @throws Exception
	 */
	@RequestMapping(value = "/addSurveyTemplate", method = RequestMethod.POST)
	public SurveyTemplate addSurveyTemplate(
			@RequestParam(value = "name", required = true) String name,
			@RequestParam(value = "description", required = true) String description) {

		return surveyService.addSurveyTemplate(name, description);
	}

	/**
	 * 
	 * @param name
	 * @return
	 */
	@GetMapping("/getSurveyTemplate/{name}")
	public SurveyTemplate getSurveyTemplateByName(@PathVariable String name) {

		return surveyService.getSurveyTemplateByName(name);
	}

	/**
	 * 
	 * @param id
	 * @return
	 */
	@GetMapping("/getSurveyTemplateById/{id}")
	public SurveyTemplate getSurveyTemplateById(@PathVariable Long id) {
		return surveyService.getSurveyTemplateById(id);
	}

	/**
	 * 
	 * @param user
	 * @throws Exception
	 */
	@RequestMapping(value = "/updateSurveyTemplate", method = RequestMethod.POST)
	public void updateSurveyTemplate(
			@RequestBody(required = true) SurveyTemplate surveyTemplate)
			throws SurveyServiceException {

		surveyService.updateSurveyTemplate(surveyTemplate);
	}

	/**
	 * 
	 * @param name
	 */
	@DeleteMapping("/removeSurveyTemplate/{name}")
	public void removeSurveyTemplate(@PathVariable String name) {

		surveyService.removeSurveyTemplate(name);
	}

	/**
	 * 
	 * @return
	 */
	@GetMapping("/allQuestionTemplates")
	public Iterable<QuestionTemplate> getQuestionTemplates() {

		return surveyService.getQuestionTemplates();
	}

	/**
	 * 
	 * @param user
	 * @throws Exception
	 */
	@RequestMapping(value = "/addQuestionTemplate", method = RequestMethod.POST)
	public QuestionTemplate addQuestionTemplate(
			@RequestParam(value = "text", required = true) String text) {

		return surveyService.addQuestionTemplate(text);
	}

	/**
	 * 
	 * @param id
	 * @return
	 */
	@GetMapping("/getQuestionTemplate/{id}")
	public QuestionTemplate getQuestionTemplateById(@PathVariable Long id)
			throws SurveyServiceException {

		return surveyService.getQuestionTemplateById(id);
	}

	/**
	 * 
	 * @param questionTemplate
	 * @throws SurveyServiceException
	 */
	@RequestMapping(value = "/updateQuestionTemplate", method = RequestMethod.POST)
	public void updateTemplateQuestion(
			@RequestBody(required = true) QuestionTemplate questionTemplate)
			throws SurveyServiceException {

		surveyService.updateTemplateQuestion(questionTemplate);
	}

	/**
	 * 
	 * @param id
	 */
	@DeleteMapping("/removeQuestionTemplate/{id}")
	public void removeQuestionTemplate(@PathVariable Long id)
			throws SurveyServiceException {

		surveyService.removeQuestionTemplate(id);
	}

	/**
	 * 
	 * @param questionTemplateId
	 * @param surveyTemplateId
	 * @param sequence
	 * @throws SurveyServiceException
	 */
	@RequestMapping(value = "/addQuestionTemplateToSurveyTemplate", method = RequestMethod.POST)
	public void addQuestionTemplateToSurveyTemplate(
			@RequestParam(value = "questionTemplateId", required = true) Long questionTemplateId,
			@RequestParam(value = "surveyTemplateId", required = true) Long surveyTemplateId,
			@RequestParam(value = "sequence", required = true) Long sequence

	) throws SurveyServiceException {
		surveyService.addQuestionTemplateToSurveyTemplate(questionTemplateId,
				surveyTemplateId, sequence);
	}

	/**
	 * 
	 * @param name
	 * @return
	 */
	@GetMapping("/healthz")
	public ServiceHealth healthz() {

		LOG.info("Checking health...");

		return new ServiceHealth("Survey", true); // TODO: Replace canned response.
	}

}
