package br.com.indtextbr.services.apigateway.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.indtextbr.services.apigateway.dto.FuncaoDTO;
import br.com.indtextbr.services.apigateway.repository.FuncaoRepository;

@Service
public class FuncaoService {
	private FuncaoRepository funcaoRepository;
	
	@Autowired
	public FuncaoService(FuncaoRepository funcaoRepository) {
		this.funcaoRepository = funcaoRepository;
	}
	
	public List<FuncaoDTO> getAllFuncao(){
		var funcoesDB = this.funcaoRepository.findAll();
		
		return funcoesDB.stream().map(p -> new FuncaoDTO(p.getId(),p.getDescricao())).collect(Collectors.toList());
	}
	
}
