package br.com.indtextbr.services.apigateway.controller;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpServerErrorException;

import br.com.indtextbr.services.apigateway.dto.UsuarioDTO;
import br.com.indtextbr.services.apigateway.dto.FuncaoDTO;
import br.com.indtextbr.services.apigateway.dto.LoginResponseDTO;
import br.com.indtextbr.services.apigateway.entity.Usuario;
import br.com.indtextbr.services.apigateway.service.FuncaoService;
import br.com.indtextbr.services.apigateway.service.UsuarioService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("api/usuarios")
public class UsuarioController {

	private UsuarioService usuarioService;
	
	private FuncaoService funcaoService;
	
	@Autowired
	public UsuarioController(UsuarioService usuarioService, FuncaoService funcaoService ) {
		this.usuarioService = usuarioService;
		this.funcaoService = funcaoService;
	}

	@PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<LoginResponseDTO> login(@RequestBody UsuarioDTO loginDto) {
		var loginResponseDTO = usuarioService.login(loginDto.getUsername(), loginDto.getPassword())
				.orElseThrow(() -> new HttpServerErrorException(HttpStatus.UNAUTHORIZED, "Falha ao realizar o login"));
		return ResponseEntity.ok(loginResponseDTO);
	}

	@GetMapping()
	public Page<UsuarioDTO> getAllUsuarios(@RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {
		PageRequest pageRequest = PageRequest.of(page, size);
		var usuarios = usuarioService.getAll(pageRequest);
		return usuarios;
	}
	
	@GetMapping("/funcoes")
	public ResponseEntity<List<FuncaoDTO>> getAllFuncoes() {
		var funcoes = funcaoService.getAllFuncao();
		return CollectionUtils.isEmpty(funcoes)? ResponseEntity.notFound().build(): ResponseEntity.ok(funcoes) ;
	}

	@PostMapping()
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<Object> criarNovo(@RequestBody @Valid UsuarioDTO loginDto) {
		var usuario = usuarioService
				.criarNovo(loginDto.getUsername(), loginDto.getPassword(), loginDto.getPrimeiroNome(),
						loginDto.getUltimoNome(), loginDto.getFuncoes())
				.orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST, "Usuário já existente"));
		URI location = URI.create(String.format("/%s", usuario.getUsername()));
		return ResponseEntity.created(location).build();
	}

	@DeleteMapping("/{username}")
	public ResponseEntity<Void> excluir(@PathVariable(value = "username") String username) {
		usuarioService.excluir(username);
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping("/{username}")
	public ResponseEntity<UsuarioDTO> getByUserName(@PathVariable(value = "username") String username) {
		var usuario = usuarioService.getByUserName(username);
		return (usuario!=null)? ResponseEntity.ok(usuario): ResponseEntity.notFound().build();
	}


	@PutMapping("/{username}")
	public ResponseEntity<UsuarioDTO> editar(@RequestBody @Valid UsuarioDTO loginDto) {
		var usuario = usuarioService
				.editar(loginDto.getUsername(), loginDto.getPassword(), loginDto.getPrimeiroNome(),
						loginDto.getUltimoNome(), loginDto.getFuncoes());
		return (usuario!=null)?ResponseEntity.ok(usuario):ResponseEntity.notFound().build();
	}

}
