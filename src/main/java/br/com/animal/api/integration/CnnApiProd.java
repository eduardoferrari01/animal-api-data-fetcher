package br.com.animal.api.integration;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@Profile("default")
public class CnnApiProd implements CnnApi{

	private static final Logger LOG = LoggerFactory.getLogger(CnnApiProd.class);
	
	@Autowired
	private CnnApiClient cnnApiClient;
	
	public List<String> getAllLabels() {

		LOG.info("Get all labels");
		return cnnApiClient.getAllLabels();
	}
	
	public IntegrationResponse classify(MultipartFile file) {
		
		LOG.info("Uploading image to API");
		return cnnApiClient.classify(file);
	}

	public Boolean existLabel(String label) {
		 
		return cnnApiClient.existLabel(label);
	}
}
