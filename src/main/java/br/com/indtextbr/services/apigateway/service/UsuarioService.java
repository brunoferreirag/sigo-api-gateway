package br.com.indtextbr.services.apigateway.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.indtextbr.services.apigateway.dto.FuncaoDTO;
import br.com.indtextbr.services.apigateway.dto.LoginResponseDTO;
import br.com.indtextbr.services.apigateway.dto.UsuarioDTO;
import br.com.indtextbr.services.apigateway.entity.Funcao;
import br.com.indtextbr.services.apigateway.entity.Usuario;
import br.com.indtextbr.services.apigateway.repository.FuncaoRepository;
import br.com.indtextbr.services.apigateway.repository.UsuarioRepository;
import br.com.indtextbr.services.apigateway.seguranca.JwtProvider;
import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
public class UsuarioService {
	
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
	 * 
	 * @param username
	 * @param password
	 * @return
	 */
	public Optional<LoginResponseDTO> login(String username, String password) {
		log.info("Novo usuário para realizar o login");
		Optional<LoginResponseDTO> loginResponse = Optional.empty();
		Optional<String> token = Optional.empty();
		Optional<Usuario> user = usuarioRepository.findByUsername(username);
		if (user.isPresent()) {
			try {
				authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
				Usuario usuario = user.get();
				var funcoes = usuario.getFuncoes();
				token = Optional.of(jwtProvider.criarToken(username, funcoes));
				var funcoesString = funcoes.stream().map(p -> p.getNomeFuncao()).collect(Collectors.toList());
				loginResponse = Optional.of(new LoginResponseDTO(usuario.getUsername(), usuario.getPrimeiroNome(),
						usuario.getUltimoNome(), token.get(), funcoesString));

			} catch (AuthenticationException e) {
				log.error("Erro no login do usuário {}", username);
			}
		}
		return loginResponse;
	}

	/**
	 * Criar o usuário
	 * 
	 * @param username
	 * @param password
	 * @param primeiroNome
	 * @param ultimoNome
	 * @return
	 */
	public Optional<Usuario> criarNovo(String username, String password, String primeiroNome, String ultimoNome,
			List<FuncaoDTO> funcoes) {
		log.info("Criar novo usuário");
		Optional<Usuario> usuario = Optional.empty();
		if (!usuarioRepository.findByUsername(username).isPresent()) {
			List<Funcao> funcoesParaInclusao = new ArrayList<>();
			funcoes.forEach(p -> {
				Optional<Funcao> funcaoDB = funcaoRepository.findById(p.getId());
				funcoesParaInclusao.add(funcaoDB.get());
			});

			usuario = Optional.of(usuarioRepository.save(new Usuario(username, passwordEncoder.encode(password),
					funcoesParaInclusao, primeiroNome, ultimoNome)));
		}
		return usuario;
	}

	/**
	 * Editar o usuário
	 * 
	 * @param username
	 * @param password
	 * @param primeiroNome
	 * @param ultimoNome
	 * @return
	 */
	public UsuarioDTO editar(String username, String password, String primeiroNome, String ultimoNome,
			List<FuncaoDTO> funcoes) {
		log.info("Editar novo usuário");
		Optional<Usuario> usuario = usuarioRepository.findByUsername(username);
		UsuarioDTO usuarioRetorno = null;
		if (usuario.isPresent()) {
			Usuario usuarioParaSalvar = usuario.get();
			usuarioParaSalvar.setPassword(passwordEncoder.encode(password));
			usuarioParaSalvar.setPrimeiroNome(primeiroNome);
			usuarioParaSalvar.setUltimoNome(ultimoNome);
			List<Funcao> funcoesParaInclusao = new ArrayList<>();
			funcoes.forEach(p -> {
				Optional<Funcao> funcaoDB = funcaoRepository.findById(p.getId());
				funcoesParaInclusao.add(funcaoDB.get());
			});
			usuarioParaSalvar.setFuncoes(funcoesParaInclusao);

			usuarioRepository.save(usuarioParaSalvar);
			usuarioRetorno = new UsuarioDTO(usuarioParaSalvar.getUsername(),"", usuarioParaSalvar.getPrimeiroNome(),usuarioParaSalvar.getUltimoNome(),usuarioParaSalvar.getFuncoes().stream()
					.map(f -> new FuncaoDTO(f.getId(),f.getDescricao())).collect(Collectors.toList()));
		}
		return usuarioRetorno;
	}

	public Page<UsuarioDTO> getAll(PageRequest pageRequest) {
		Page<Usuario> resultadoPaginado =  usuarioRepository.findAll(pageRequest);
		List<UsuarioDTO> todos = resultadoPaginado
			      .stream()
			      .map( p -> new UsuarioDTO(
			    		  p.getUsername(),"", 
			    		  p.getPrimeiroNome(), 
			    		  p.getUltimoNome(),
			    		  p.getFuncoes().stream()
			    		 .map(f -> new FuncaoDTO(f.getId(),f.getDescricao())).collect(Collectors.toList()))).collect(Collectors.toList());
			 
	   return new PageImpl<>(todos, pageRequest, resultadoPaginado.getTotalElements());
			 
	}

	public void excluir(String username) {
		Optional<Usuario> usuario = usuarioRepository.findByUsername(username);
		if (usuario.isPresent()) {
			Usuario usuarioParaExcluir = usuario.get();
			usuarioRepository.delete(usuarioParaExcluir);
		}
	}
	
	public UsuarioDTO getByUserName(String username) {
		Optional<Usuario> usuario = usuarioRepository.findByUsername(username);
		if (usuario.isPresent()) {
			Usuario usuarioNoBD = usuario.get();
			return new UsuarioDTO(usuarioNoBD.getUsername(),"", usuarioNoBD.getPrimeiroNome(),usuarioNoBD.getUltimoNome(),usuarioNoBD.getFuncoes().stream()
					.map(f -> new FuncaoDTO(f.getId(),f.getDescricao())).collect(Collectors.toList()));
		}
		return null;
	}
}