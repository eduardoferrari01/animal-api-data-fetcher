package br.com.animal.api.domain;

public enum ConservationState {

	EX("Extinta"), EW("Extinta na natureza"), CR("Criticamente em perigo"), EN("Em perigo"), VU("Vulnerável"),
	NT("Quase ameaçada"), LC("Pouco preocupante"), DD("Dados Deficientes"), NE("Não avaliada"), NI("Não informado");

	public final String label;

	private ConservationState(String label) {

		this.label = label;
	}
	
	public String getLabel() {
		return this.label;
	}
}