package br.com.indtextbr.services.apigateway.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDTO {
	@JsonProperty("user-name")
	private String userName;
	@JsonProperty("primeiro-nome")
	private String primeiroNome;
	@JsonProperty("ultimo-nome")
	private String ultimoNome;
	private String token;
	private List<String> funcoes;
}
