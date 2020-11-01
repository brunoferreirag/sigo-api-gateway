package br.com.indtextbr.services.apigateway.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import java.util.List;

/**
 * Security User Entity.
 *
 * Created by Mary Ellen Bowman
 */
@Entity
@Table(name = "usuario_seguranca")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    @JsonIgnore
    private String password;

    @Column(name = "primeiro_nome")
    @JsonProperty("primeiro-nome")
    private String primeiroNome;

    @Column(name = "ultimo_nome")
    @JsonProperty("ultimo-nome")
    private String ultimoNome;


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "usuario_funcao", joinColumns
            = @JoinColumn(name = "usuario_id",
            referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "funcao_id",
                    referencedColumnName = "id"))


    private List<Funcao> funcoes;
    
    public Usuario(String username, String password, List<Funcao> funcoes, String primeiroNome, String ultimoNome) {
        this.username = username;
        this.password = password;
        this.funcoes = funcoes;
        this.primeiroNome = primeiroNome;
        this.ultimoNome = ultimoNome;
    }


}
