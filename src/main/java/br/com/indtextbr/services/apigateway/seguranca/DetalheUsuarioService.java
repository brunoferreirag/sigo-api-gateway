package br.com.indtextbr.services.apigateway.seguranca;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import br.com.indtextbr.services.apigateway.entity.Usuario;
import br.com.indtextbr.services.apigateway.repository.UsuarioRepository;

import java.util.Optional;

import static org.springframework.security.core.userdetails.User.withUsername;


@Component
public class DetalheUsuarioService implements UserDetailsService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    JwtProvider jwtProvider;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByUsername(s).orElseThrow(() ->
                new UsernameNotFoundException(String.format("User with name %s does not exist", s)));

        return withUsername(usuario.getUsername())
            .password(usuario.getPassword())
            .authorities(usuario.getFuncoes())
            .accountExpired(false)
            .accountLocked(false)
            .credentialsExpired(false)
            .disabled(false)
            .build();
    }

    
    public Optional<UserDetails> loadUserByJwtToken(String jwtToken) {
        if (jwtProvider.isTokenValido(jwtToken)) {
            return Optional.of(
                withUsername(jwtProvider.getUsername(jwtToken))
                .authorities(jwtProvider.getFuncoes(jwtToken))
                .password("") //não pode ficar vazio
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build());
        }
        return Optional.empty();
    }


    public Optional<UserDetails> loadUserByJwtTokenAndDatabase(String jwtToken) {
        if (jwtProvider.isTokenValido(jwtToken)) {
            return Optional.of(loadUserByUsername(jwtProvider.getUsername(jwtToken)));
        } else {
            return Optional.empty();
        }
    }
}