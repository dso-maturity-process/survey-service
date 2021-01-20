import org.springframework.cloud.contract.spec.Contract

Contract.make {
	description "should return question template by id"

	request {
		url "/survey/removeQuestionTemplate/10001"
		method DELETE()
	}

	response {
		status OK()
		headers {
		}
	}
}