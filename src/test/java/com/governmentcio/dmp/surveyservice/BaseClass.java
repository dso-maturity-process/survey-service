package com.governmentcio.dmp.surveyservice;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.governmentcio.dmp.Application;
import com.governmentcio.dmp.model.QuestionTemplate;
import com.governmentcio.dmp.model.SurveyTemplate;
import com.governmentcio.dmp.surveyservice.controller.SurveyServiceController;
import com.governmentcio.dmp.surveyservice.service.SurveyService;

import io.restassured.module.mockmvc.RestAssuredMockMvc;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public abstract class BaseClass {

	@Autowired
	SurveyServiceController surveyServiceController;

	@MockBean
	SurveyService surveyService;

	@Before
	public void setup() {
		RestAssuredMockMvc.standaloneSetup(surveyServiceController);

		SurveyTemplate surveyTemplate = new SurveyTemplate(10001L,
				"VeteransAdministration-DSO");

		surveyTemplate.setDescription("Primary survey for the VA");

		QuestionTemplate questionTemplate = new QuestionTemplate(20001L,
				"Text for the first question");

		surveyTemplate.getQuestionTemplates().add(questionTemplate);

		questionTemplate = new QuestionTemplate(20002L,
				"Text for the second question");

		surveyTemplate.getQuestionTemplates().add(questionTemplate);

		Mockito
				.when(surveyServiceController
						.getSurveyTemplateByName("VeteransAdministration-DSO"))
				.thenReturn(surveyTemplate);
	}

}
