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
public class FuncaoDTO {
	@JsonProperty("id")
	private Long id;
	@JsonProperty("descricao")
	private String descricao;
}
