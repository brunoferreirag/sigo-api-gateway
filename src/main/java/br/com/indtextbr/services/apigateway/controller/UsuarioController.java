package br.com.indtextbr.services.apigateway.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpServerErrorException;

import br.com.indtextbr.services.apigateway.dto.LoginDto;
import br.com.indtextbr.services.apigateway.entity.Usuario;
import br.com.indtextbr.services.apigateway.service.UsuarioService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("api/v1/usuarios")
public class UsuarioController {
	
	@Autowired
	private UsuarioService usuarioService;

	@PostMapping("/login")
	public String login(@RequestBody @Valid LoginDto loginDto) {
		return usuarioService.login(loginDto.getUsername(), loginDto.getPassword())
				.orElseThrow(() -> new HttpServerErrorException(HttpStatus.UNAUTHORIZED, "Falha ao realizar o login"));
	}

	@GetMapping()
	public List<Usuario> getAllUsuarios() {
		return usuarioService.getAll();
	}

	@PostMapping()
	@ResponseStatus(HttpStatus.CREATED)
	public Usuario criarNovo(@RequestBody @Valid LoginDto loginDto) {
		return usuarioService
				.criarNovo(loginDto.getUsername(), loginDto.getPassword(), loginDto.getPrimeiroNome(), loginDto.getUltimoNome(),loginDto.getFuncoes())
				.orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST, "Usuário já existente"));
	}

}
