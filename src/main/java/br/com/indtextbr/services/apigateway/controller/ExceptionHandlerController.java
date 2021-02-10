package br.com.indtextbr.services.apigateway.controller;

import java.util.Collections;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpServerErrorException;

import br.com.indtextbr.services.apigateway.dto.ErroDTO;
import lombok.extern.log4j.Log4j2;


@ControllerAdvice
@Log4j2
public class ExceptionHandlerController {
	
	@ExceptionHandler(HttpServerErrorException.class)
    public ResponseEntity<List<ErroDTO>> handlerHttpServerErrorException(HttpServerErrorException ex) {
        List<ErroDTO> erros = Collections.singletonList(new ErroDTO(ex.getMessage()));
        return new ResponseEntity<>(erros, ex.getStatusCode());
    }
	
	
	@ExceptionHandler(RuntimeException.class)
    public ResponseEntity<List<ErroDTO>> handlerInternalServerErrorException(RuntimeException ex) {
        log.error("internal server error", ex);
        List<ErroDTO> erros = Collections.singletonList(new ErroDTO("Erro interno no servidor"));
        return new ResponseEntity<>(erros, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
