package br.com.indtextbr.services.apigateway.dto;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginDto {
    @NotNull
    private String username;

    @NotNull
    private String password;

    @JsonProperty("primeiro-nome")
    private String primeiroNome;

    @JsonProperty("ultimo-nome")
    private String ultimoNome;
    
    @JsonProperty("funcoes")
    private List<String> funcoes;


    /**
     * Partial constructor
     * @param username
     * @param password
     */
    public LoginDto(String username, String password) {
        this.username = username;
        this.password = password;
    }
   
}
