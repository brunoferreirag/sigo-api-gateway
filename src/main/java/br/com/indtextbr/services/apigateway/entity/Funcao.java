package br.com.indtextbr.services.apigateway.entity;

import org.springframework.security.core.GrantedAuthority;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name="funcao_seguranca")
@Getter
@Setter
public class Funcao  implements GrantedAuthority {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="nome_funcao")
    @JsonProperty("nome-funcao")
    private String nomeFuncao;

    @Column(name="descricao")
    private String descricao;

    @Override
    public String getAuthority() {
        return nomeFuncao;
    }

}