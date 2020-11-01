package br.com.indtextbr.services.apigateway.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.indtextbr.services.apigateway.entity.Funcao;
import br.com.indtextbr.services.apigateway.entity.Usuario;
import br.com.indtextbr.services.apigateway.repository.FuncaoRepository;
import br.com.indtextbr.services.apigateway.repository.UsuarioRepository;
import br.com.indtextbr.services.apigateway.seguranca.JwtProvider;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UsuarioService.class);

    private UsuarioRepository usuarioRepository;

    private AuthenticationManager authenticationManager;

    private FuncaoRepository funcaoRepository;

    private PasswordEncoder passwordEncoder;

    private JwtProvider jwtProvider;

    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository, AuthenticationManager authenticationManager,
                       FuncaoRepository funcaoRepository, PasswordEncoder passwordEncoder, JwtProvider jwtProvider) {
        this.usuarioRepository = usuarioRepository;
        this.authenticationManager = authenticationManager;
        this.funcaoRepository = funcaoRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
    }

    /**
     * Autenticar o usuário
     * @param username
     * @param password
     * @return
     */
    public Optional<String> login(String username, String password) {
        LOGGER.info("Novo usuário para realizar o login");
        Optional<String> token = Optional.empty();
        Optional<Usuario> user = usuarioRepository.findByUsername(username);
        if (user.isPresent()) {
            try {
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
                token = Optional.of(jwtProvider.criarToken(username, user.get().getFuncoes()));
            } catch (AuthenticationException e){
                LOGGER.error("Erro no login do usuário {}", username);
            }
        }
        return token;
    }

   /**
    * Criar o usuário
    * @param username
    * @param password
    * @param primeiroNome
    * @param ultimoNome
    * @return
    */
    public Optional<Usuario> criarNovo(String username, String password, String primeiroNome, String ultimoNome, List<String> funcoes) {
        LOGGER.info("Criar novo usuário");
        Optional<Usuario> usuario = Optional.empty();
        if (!usuarioRepository.findByUsername(username).isPresent()) {
        	List<Funcao> funcoesParaInclusao = new ArrayList<>();
        	funcoes.forEach(p->{
        		Optional<Funcao> funcaoDB = funcaoRepository.findByNomeFuncao(p);
        		funcoesParaInclusao.add(funcaoDB.get());
        	});
        	
            usuario = Optional.of(usuarioRepository.save(new Usuario(username,
                            passwordEncoder.encode(password),
                            funcoesParaInclusao,
                            primeiroNome,
                            ultimoNome)));
        }
        return usuario;
    }

    public List<Usuario> getAll() {
        return usuarioRepository.findAll();
    }
}