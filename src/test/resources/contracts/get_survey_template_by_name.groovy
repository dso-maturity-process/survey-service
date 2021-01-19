import org.springframework.cloud.contract.spec.Contract

Contract.make {
	description "should return survey template named VeteransAdministration-DSO"

	request {
		url "/survey/getSurveyTemplate/VeteransAdministration-DSO"
		method GET()
	}

	response {
		status OK()
		headers {
			contentType applicationJson()
		}
		body (
			id: 10001,
			name: "VeteransAdministration-DSO"
		)
	}
}