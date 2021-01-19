package com.governmentcio.dmp.surveyservice;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.governmentcio.dmp.Application;
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

		Mockito
				.when(surveyServiceController
						.getSurveyTemplateByName("VeteransAdministration-DSO"))
				.thenReturn(new SurveyTemplate(10001L, "VeteransAdministration-DSO"));
	}

}
