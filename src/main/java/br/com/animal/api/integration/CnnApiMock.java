package br.com.animal.api.integration;

import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@Profile("test")
public class CnnApiMock implements CnnApi{

	private List<String> labels;
	
	public CnnApiMock() {
		
		labels = Arrays.asList("dendroaspis-polylepis", "oxyuranus-microlepidotus", "titanoboa-cerrejonensis", "ophiophagus-hannah",
				"theraphosa-blondi", "malayopython-reticulatus");
	}
	
	@Override
	public List<String> getAllLabels() {
		
		return  labels;
	}

	@Override
	public IntegrationResponse classify(MultipartFile file) {
		 
		IntegrationResponse  integrationResponse = new IntegrationResponse();
		
		if("unknown.png".equals(file.getOriginalFilename())) {
			
			integrationResponse.setLabel("unknown");
			
			return integrationResponse;
		}
		
		integrationResponse.setLabel("dendroaspis-polylepis");
		
		return integrationResponse;
	}

	@Override
	public Boolean existLabel(String label) {

		return labels.contains(label);
	}

}
