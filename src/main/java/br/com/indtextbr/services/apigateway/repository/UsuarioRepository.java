package br.com.indtextbr.services.apigateway.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import br.com.indtextbr.services.apigateway.entity.Usuario;

import java.util.Optional;


@Repository
public interface UsuarioRepository extends PagingAndSortingRepository<Usuario, Integer> {
    Optional<Usuario> findByUsername(String userName);
}