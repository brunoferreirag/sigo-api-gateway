package br.com.indtextbr.services.apigateway.dto;

import java.util.List;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
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
public class UsuarioDTO {
    @NotNull
    @NotBlank(message = "User Name tem que ser preenchido")
    @Email(message = "User Name deve ser um e-mail válido")
    private String username;

    @NotNull
    @NotBlank(message = "Password tem que ser preenchido")
    private String password;

    @JsonProperty("primeiro-nome")
    private String primeiroNome;

    @JsonProperty("ultimo-nome")
    private String ultimoNome;
    
    @JsonProperty("funcoes")
    private List<FuncaoDTO> funcoes;


    /**
     * Partial constructor
     * @param username
     * @param password
     */
    public UsuarioDTO(String username, String password) {
        this.username = username;
        this.password = password;
    }
   
}
