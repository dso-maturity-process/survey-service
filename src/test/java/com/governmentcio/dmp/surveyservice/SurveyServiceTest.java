
package com.governmentcio.dmp.surveyservice;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.governmentcio.dmp.Application;
import com.governmentcio.dmp.dao.SurveyTemplateQuestionTemplateDao;
import com.governmentcio.dmp.exception.SurveyServiceException;
import com.governmentcio.dmp.model.QuestionTemplate;
import com.governmentcio.dmp.model.SurveyTemplate;
import com.governmentcio.dmp.surveyservice.service.SurveyService;

/**
 * 
 * @author <a href=mailto:support@governmentcio.com>support</a>
 * 
 *         Tests for Assessment service controller
 * 
 *         See import.sql for the data that is pre-loaded before tests are run
 *         which many of the tests depend on the contents of the database.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@Transactional
class SurveyServiceTest {

	@Autowired
	SurveyService surveyService;

	/**
	 * 
	 */
	@Test
	public void test_Getting_SurveyTemplates() {

		Iterable<SurveyTemplate> sts = surveyService.getSurveyTemplates();

		assertNotNull(sts);
	}

	/**
	 * 
	 */
	@Test
	public void test_Get_All_SurveytemplateQuestiontemplateAssociations() {

		Iterable<SurveyTemplateQuestionTemplateDao> stqts = surveyService
				.getSurveytemplateQuestiontemplateAssociations();

		assertNotNull(stqts);

		for (SurveyTemplateQuestionTemplateDao stqt : stqts) {
			assertNotNull(stqt);
		}

	}

	/**
	 * 
	 */
	@Test
	public void test_getSurveyTemplate_by_ID() {

		SurveyTemplate st = surveyService.getSurveyTemplateById(10001L);

		assertNotNull(st);
		assertTrue(st.getName().equals("Veterans Administration-DSO"));
		assertTrue(st.getDescription().equals("Primary survey for the VA"));

		Set<QuestionTemplate> qts = st.getQuestionTemplates();

		assertNotNull(qts);

		assertTrue(qts.size() == 3);

		for (QuestionTemplate qt : qts) {
			assertNotNull(qt);
		}
	}

	/**
	 * 
	 */
	@Test
	public void test_SurveyTemplate_CRUD_Functionality()
			throws SurveyServiceException {

		String templateName = "Test Survey1";
		String templateDescription = "This is just a test template";
		String updatedTemplateName = " Updated Test Survey";

		SurveyTemplate newST = surveyService.addSurveyTemplate(templateName,
				templateDescription);

		assertNotNull(newST);

		SurveyTemplate queriedST = surveyService
				.getSurveyTemplateById(newST.getId());

		assertNotNull(queriedST);

		assertTrue(newST.getName().equals(templateName));
		assertTrue(newST.getDescription().equals(templateDescription));

		queriedST = surveyService.getSurveyTemplateByName(newST.getName());

		assertNotNull(queriedST);

		assertTrue(newST.getName().equals(templateName));
		assertTrue(newST.getDescription().equals(templateDescription));

		queriedST.setName(updatedTemplateName);

		surveyService.updateSurveyTemplate(queriedST);

		queriedST = surveyService.getSurveyTemplateByName(queriedST.getName());

		assertNotNull(queriedST);

		assertTrue(queriedST.getName().equals(updatedTemplateName));

		surveyService.removeSurveyTemplate(queriedST.getName());

		queriedST = surveyService.getSurveyTemplateByName(queriedST.getName());

		assertNull(queriedST);

	}

	/**
	 * 
	 */
	@Test
	@Transactional
	public void test_QuestionTemplate_CRUD_Functionality()
			throws SurveyServiceException {

		Iterable<QuestionTemplate> qts = surveyService.getQuestionTemplates();

		assertNotNull(qts);

		String qtText = "This is a new question template";
		String qtUpdatedText = "This is the updated question template text";

		QuestionTemplate newQt = new QuestionTemplate();
		newQt.setText(qtText);
		newQt.setSequence(10L);

		newQt = surveyService.addQuestionTemplate(qtText);

		assertNotNull(newQt);

		assertTrue(newQt.getText().equals(qtText));

		qts = surveyService.getQuestionTemplates();

		assertNotNull(qts);

		newQt.setText(qtUpdatedText);

		surveyService.updateTemplateQuestion(newQt);

		QuestionTemplate updatedQt = surveyService
				.getQuestionTemplateById(newQt.getId());

		assertNotNull(updatedQt);

		assertTrue(updatedQt.getText().equals(qtUpdatedText));

		surveyService.removeQuestionTemplate(newQt.getId());

		assertNull(surveyService.getQuestionTemplateById(newQt.getId()));

	}

	/**
	 * 
	 */
	@Test
	@Transactional
	public void test_Adding_QuestionTemplates_to_SurveyTemplate()
			throws SurveyServiceException {

		String templateName = "Test Survey2";
		String templateDescription = "This is just a test template";

		SurveyTemplate newST = surveyService.addSurveyTemplate(templateName,
				templateDescription);

		assertNotNull(newST);

		QuestionTemplate firstNewQt = new QuestionTemplate();

		String qtFirstText = "This is the first question";
		firstNewQt = surveyService.addQuestionTemplate(qtFirstText);

		assertNotNull(firstNewQt);

		surveyService.addQuestionTemplateToSurveyTemplate(firstNewQt.getId(),
				newST.getId(), 1L);

		QuestionTemplate secondNewQt = new QuestionTemplate();

		String qtSecondText = "This is the second question";
		secondNewQt = surveyService.addQuestionTemplate(qtSecondText);

		assertNotNull(secondNewQt);

		surveyService.addQuestionTemplateToSurveyTemplate(secondNewQt.getId(),
				newST.getId(), 2L);

		SurveyTemplate queriedST = surveyService
				.getSurveyTemplateByName(newST.getName());

		assertNotNull(queriedST);

		Set<QuestionTemplate> qts = queriedST.getQuestionTemplates();

		for (QuestionTemplate qt : qts) {
			assertNotNull(qt);
		}

		surveyService.removeQuestionTemplateFromSurveyTemplate(firstNewQt.getId(),
				newST.getId());

		// Check if the QT was removed from ST
		queriedST = surveyService.getSurveyTemplateByName(newST.getName());

		assertNotNull(queriedST);

		qts = queriedST.getQuestionTemplates();

		for (QuestionTemplate qt : qts) {
			assertFalse(qt.getText().equals(firstNewQt.getText()));
			System.out.println(qt);
		}

		surveyService.removeQuestionTemplate(firstNewQt.getId());
		surveyService.removeQuestionTemplate(secondNewQt.getId());

		surveyService.removeSurveyTemplate(newST.getName());

		queriedST = surveyService.getSurveyTemplateByName(newST.getName());

		assertNull(queriedST);

	}

}
