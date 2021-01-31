
package com.governmentcio.dmp.surveyservice;

import static org.junit.Assert.assertFalse;

/**
 * 
 *  @author <a href=mailto:support@governmentcio.com>support</a>
 *  
 */

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.governmentcio.dmp.Application;
import com.governmentcio.dmp.dao.QuestionTemplateDao;
import com.governmentcio.dmp.model.QuestionTemplate;
import com.governmentcio.dmp.model.SurveyTemplate;
import com.governmentcio.dmp.utility.ServiceHealth;

/**
 * 
 * @author <a href=mailto:support@governmentcio.com>support</a>
 * 
 *         Tests for Assessment service controller
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SurveyServiceControllerTest {

	@LocalServerPort
	private int port;

	TestRestTemplate restTemplate = new TestRestTemplate();

	HttpHeaders headers = new HttpHeaders();

	private static final String BASE_URL = "/survey";

	/**
	 * 
	 */
	@Test
	public void test_Getting_all_Questions() {

		HttpEntity<String> entity = new HttpEntity<String>(null, headers);

		ResponseEntity<
				Iterable<QuestionTemplateDao>> response = restTemplate.exchange(
						createURLWithPort("/allQuestionTemplates"), HttpMethod.GET, entity,
						new ParameterizedTypeReference<Iterable<QuestionTemplateDao>>() {
						});

		assertNotNull(response);

	}

	/**
	 * 
	 */
	@Test
	public void test_getSurveyTemplate_by_ID() {

		HttpEntity<String> entity = new HttpEntity<String>(null, headers);

		String name = "Test Template for GetByID";
		String description = "Testing only";

		String parameters = "?name=" + name + "&description=" + description;

		ResponseEntity<SurveyTemplate> response = restTemplate.exchange(
				createURLWithPort("/addSurveyTemplate" + parameters), HttpMethod.POST,
				entity, new ParameterizedTypeReference<SurveyTemplate>() {
				});

		assertNotNull(response);

		assertTrue(response.getStatusCode() == HttpStatus.OK);

		SurveyTemplate surveyTemplate = response.getBody();

		assertNotNull(surveyTemplate);

		assertTrue(surveyTemplate.getName().equals(name));
		assertTrue(surveyTemplate.getDescription().equals(description));

		// Get the SurveyTemplate just added

		response = restTemplate.exchange(
				createURLWithPort("/getSurveyTemplateById/" + surveyTemplate.getId()),
				HttpMethod.GET, entity,
				new ParameterizedTypeReference<SurveyTemplate>() {
				});

		assertNotNull(response);

		assertTrue(response.getStatusCode() == HttpStatus.OK);

		surveyTemplate = response.getBody();

		assertNotNull(surveyTemplate);

		assertTrue(surveyTemplate.getName().equals(name));
		assertTrue(surveyTemplate.getDescription().equals(description));

		// Remove the Survey template

		ResponseEntity<Void> responseVoid = restTemplate.exchange(
				createURLWithPort("/removeSurveyTemplate/" + surveyTemplate.getName()),
				HttpMethod.DELETE, entity, new ParameterizedTypeReference<Void>() {
				});

		assertNotNull(responseVoid);

		assertTrue(responseVoid.getStatusCode() == HttpStatus.OK);

	}

	/**
	 * 
	 */
	@Test
	public void test_SurveyTemplate_CRUD_Functionality() {

		HttpEntity<String> entity = new HttpEntity<String>(null, headers);

		String name = "VA Secondary Survey";
		String description = "A backup survey for the VA";

		String parameters = "?name=" + name + "&description=" + description;

		ResponseEntity<SurveyTemplate> response = restTemplate.exchange(
				createURLWithPort("/addSurveyTemplate" + parameters), HttpMethod.POST,
				entity, new ParameterizedTypeReference<SurveyTemplate>() {
				});

		assertNotNull(response);

		assertTrue(response.getStatusCode() == HttpStatus.OK);

		SurveyTemplate surveyTemplate = response.getBody();

		assertNotNull(surveyTemplate);

		assertTrue(surveyTemplate.getName().equals(name));
		assertTrue(surveyTemplate.getDescription().equals(description));

		// Get the SurveyTemplate just added

		response = restTemplate.exchange(
				createURLWithPort("/getSurveyTemplate/" + surveyTemplate.getName()),
				HttpMethod.GET, entity,
				new ParameterizedTypeReference<SurveyTemplate>() {
				});

		assertNotNull(response);

		assertTrue(response.getStatusCode() == HttpStatus.OK);

		surveyTemplate = response.getBody();

		assertNotNull(surveyTemplate);

		assertTrue(surveyTemplate.getName().equals(name));
		assertTrue(surveyTemplate.getDescription().equals(description));

		// Update the Survey Template
		String updatedName = "Updated Survey Name";

		surveyTemplate.setName(updatedName);

		// Prepare acceptable media type
		List<MediaType> acceptableMediaTypes = new ArrayList<MediaType>();
		acceptableMediaTypes.add(MediaType.APPLICATION_JSON);

		// Prepare header
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(acceptableMediaTypes);
		HttpEntity<SurveyTemplate> updatedSurveyTemplateEntity = new HttpEntity<
				SurveyTemplate>(surveyTemplate, headers);

		response = restTemplate.exchange(createURLWithPort("/updateSurveyTemplate"),
				HttpMethod.POST, updatedSurveyTemplateEntity, SurveyTemplate.class);

		assertNotNull(response);

		assertTrue(response.getStatusCode() == HttpStatus.OK);

		// Remove the Survey template

		ResponseEntity<Void> responseVoid = restTemplate.exchange(
				createURLWithPort("/removeSurveyTemplate/" + surveyTemplate.getName()),
				HttpMethod.DELETE, entity, new ParameterizedTypeReference<Void>() {
				});

		assertNotNull(responseVoid);

		assertTrue(responseVoid.getStatusCode() == HttpStatus.OK);

		// Ensure User deleted

		response = restTemplate.exchange(
				createURLWithPort("/getSurveyTemplate/" + surveyTemplate.getName()),
				HttpMethod.GET, entity,
				new ParameterizedTypeReference<SurveyTemplate>() {
				});

		assertNotNull(response);

		assertTrue(response.getStatusCode() == HttpStatus.OK);

		assertNull(response.getBody());

	}

	/**
	 * 
	 */
	@Test
	public void test_QuestionTemplate_CRUD_Functionality() {

		HttpEntity<String> entity = new HttpEntity<String>(null, headers);

		// Create a new question template

		String text = "Test Question 101";
		String parameters = "?text=" + text;

		ResponseEntity<QuestionTemplate> response = restTemplate.exchange(
				createURLWithPort("/addQuestionTemplate" + parameters), HttpMethod.POST,
				entity, new ParameterizedTypeReference<QuestionTemplate>() {
				});

		assertNotNull(response);

		assertTrue(response.getStatusCode() == HttpStatus.OK);

		QuestionTemplate newQuestionTemplate = response.getBody();

		assertNotNull(newQuestionTemplate);

		assertTrue(newQuestionTemplate.getText().equals(text));

		// Get the QuestionTemplate just added

		response = restTemplate.exchange(
				createURLWithPort(
						"/getQuestionTemplate/" + newQuestionTemplate.getId()),
				HttpMethod.GET, entity,
				new ParameterizedTypeReference<QuestionTemplate>() {
				});

		assertNotNull(response);

		assertTrue(response.getStatusCode() == HttpStatus.OK);

		QuestionTemplate retrievedQuestion = response.getBody();

		assertNotNull(retrievedQuestion);

		assertTrue(retrievedQuestion.getId().equals(newQuestionTemplate.getId()));
		assertTrue(
				retrievedQuestion.getText().equals(newQuestionTemplate.getText()));

		// Update the question template with new text

		String updatedText = "This is an update to the question template";

		newQuestionTemplate.setText(updatedText);

		// Prepare acceptable media type
		List<MediaType> acceptableMediaTypes = new ArrayList<MediaType>();
		acceptableMediaTypes.add(MediaType.APPLICATION_JSON);

		// Prepare header
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(acceptableMediaTypes);
		HttpEntity<QuestionTemplate> updatedQuestionEntity = new HttpEntity<
				QuestionTemplate>(newQuestionTemplate, headers);

		response = restTemplate.exchange(
				createURLWithPort("/updateQuestionTemplate"), HttpMethod.POST,
				updatedQuestionEntity, QuestionTemplate.class);

		assertNotNull(response);

		assertTrue(response.getStatusCode() == HttpStatus.OK);

		// Remove the QuestionTemplate

		ResponseEntity<Void> responseVoid = restTemplate.exchange(
				createURLWithPort(
						"/removeQuestionTemplate/" + newQuestionTemplate.getId()),
				HttpMethod.DELETE, entity, new ParameterizedTypeReference<Void>() {
				});

		assertNotNull(responseVoid);

		assertTrue(responseVoid.getStatusCode() == HttpStatus.OK);

		// Ensure QuestionTemplate removed

		response = restTemplate.exchange(
				createURLWithPort(
						"/getQuestionTemplate/" + newQuestionTemplate.getId()),
				HttpMethod.GET, entity,
				new ParameterizedTypeReference<QuestionTemplate>() {
				});

		assertNotNull(response);

		assertTrue(response.getStatusCode() == HttpStatus.OK);

		retrievedQuestion = response.getBody();

		assertNull(retrievedQuestion);

	}

	/**
	 *
	 */
	@Test
	public void testHealth() throws JSONException {

		HttpEntity<String> entity = new HttpEntity<String>(null, headers);

		ResponseEntity<ServiceHealth> response = restTemplate.exchange(
				createURLWithPort("/healthz"), HttpMethod.GET, entity,
				new ParameterizedTypeReference<ServiceHealth>() {
				});

		assertNotNull(response);

		assertTrue(response.getStatusCode() == HttpStatus.OK);

		ServiceHealth srvHealth = response.getBody();

		assertNotNull(srvHealth);

		assertTrue(srvHealth.isHealthy());
	}

	/**
	 * 
	 */
	@Test
	public void test_Adding_QuestionTemplates_to_SurveyTemplate() {

		HttpEntity<String> entity = new HttpEntity<String>(null, headers);

		String name = "Survey with Questions";
		String description = "Testing adding question template to a survey template";

		String parameters = "?name=" + name + "&description=" + description;

		ResponseEntity<SurveyTemplate> surveyTemplateResponse = restTemplate
				.exchange(createURLWithPort("/addSurveyTemplate" + parameters),
						HttpMethod.POST, entity,
						new ParameterizedTypeReference<SurveyTemplate>() {
						});

		assertNotNull(surveyTemplateResponse);

		assertTrue(surveyTemplateResponse.getStatusCode() == HttpStatus.OK);

		SurveyTemplate surveyTemplate = surveyTemplateResponse.getBody();

		assertNotNull(surveyTemplate);

		assertTrue(surveyTemplate.getName().equals(name));
		assertTrue(surveyTemplate.getDescription().equals(description));

		// Create a new question template

		String text = "Test Question 110";
		parameters = "?text=" + text;

		ResponseEntity<QuestionTemplate> questionTemplateResponse = restTemplate
				.exchange(createURLWithPort("/addQuestionTemplate" + parameters),
						HttpMethod.POST, entity,
						new ParameterizedTypeReference<QuestionTemplate>() {
						});

		assertNotNull(questionTemplateResponse);

		assertTrue(questionTemplateResponse.getStatusCode() == HttpStatus.OK);

		QuestionTemplate newQuestionTemplate = questionTemplateResponse.getBody();

		assertNotNull(newQuestionTemplate);

		assertTrue(newQuestionTemplate.getText().equals(text));

		/**
		 * Add QuestionTemplate to SurveyTemplate
		 */

		parameters = "?questionTemplateId=" + newQuestionTemplate.getId()
				+ "&surveyTemplateId=" + surveyTemplate.getId() + "&sequence=" + 1L;

		ResponseEntity<Void> responseVoid = restTemplate.exchange(
				createURLWithPort("/addQuestionTemplateToSurveyTemplate" + parameters),
				HttpMethod.POST, entity, new ParameterizedTypeReference<Void>() {
				});

		assertNotNull(responseVoid);

		assertTrue(responseVoid.getStatusCode() == HttpStatus.OK);

		// Create a second question template

		text = "Test Question 102";
		parameters = "?text=" + text;

		questionTemplateResponse = restTemplate.exchange(
				createURLWithPort("/addQuestionTemplate" + parameters), HttpMethod.POST,
				entity, new ParameterizedTypeReference<QuestionTemplate>() {
				});

		assertNotNull(questionTemplateResponse);

		assertTrue(questionTemplateResponse.getStatusCode() == HttpStatus.OK);

		newQuestionTemplate = questionTemplateResponse.getBody();

		assertNotNull(newQuestionTemplate);

		assertTrue(newQuestionTemplate.getText().equals(text));

		/**
		 * Add a second QuestionTemplate
		 */
		parameters = "?questionTemplateId=" + newQuestionTemplate.getId()
				+ "&surveyTemplateId=" + surveyTemplate.getId() + "&sequence=" + 2L;

		responseVoid = restTemplate.exchange(
				createURLWithPort("/addQuestionTemplateToSurveyTemplate" + parameters),
				HttpMethod.POST, entity, new ParameterizedTypeReference<Void>() {
				});

		assertNotNull(responseVoid);

		assertTrue(responseVoid.getStatusCode() == HttpStatus.OK);

		// Get the SurveyTemplate with QuestionTemplates added

		surveyTemplateResponse = restTemplate.exchange(
				createURLWithPort("/getSurveyTemplate/" + surveyTemplate.getName()),
				HttpMethod.GET, entity,
				new ParameterizedTypeReference<SurveyTemplate>() {
				});

		assertNotNull(surveyTemplateResponse);

		assertTrue(surveyTemplateResponse.getStatusCode() == HttpStatus.OK);

		surveyTemplate = surveyTemplateResponse.getBody();

		assertNotNull(surveyTemplate);

		assertTrue(surveyTemplate.getName().equals(name));
		assertTrue(surveyTemplate.getDescription().equals(description));

		Set<QuestionTemplate> questionTemplates = surveyTemplate
				.getQuestionTemplates();

		assertFalse(questionTemplates.isEmpty());

	}

	/**
	 * Returns a valid URL for local host, available port and user supplied
	 * mapping.
	 * 
	 * @param string Mapping to controller function.
	 * @return Valid URL for local host and port.
	 */
	private String createURLWithPort(String uri) {
		return "http://localhost:" + port + BASE_URL + uri;
	}

}
