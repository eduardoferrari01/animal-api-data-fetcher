package br.com.animal.api.integration;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CnnApi {

	@Autowired
	private CnnApiClient cnnApiClient;

	public List<String> getAllLabels() {

		return cnnApiClient.getAllLabels();
	}
}
