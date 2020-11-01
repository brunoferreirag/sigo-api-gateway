package br.com.indtextbr.services.apigateway.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.indtextbr.services.apigateway.entity.Funcao;


public interface FuncaoRepository extends JpaRepository<Funcao, Long> {
    Optional<Funcao> findByNomeFuncao(String name);
}
