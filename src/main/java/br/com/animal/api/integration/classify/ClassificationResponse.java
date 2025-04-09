package br.com.animal.api.integration.classify;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ClassificationResponse(String label, Double probability) {

}