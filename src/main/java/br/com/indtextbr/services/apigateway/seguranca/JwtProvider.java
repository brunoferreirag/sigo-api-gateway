package br.com.indtextbr.services.apigateway.seguranca;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import br.com.indtextbr.services.apigateway.entity.Funcao;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class JwtProvider{

    private final String FUNCOES_CHAVE = "funcoes";

    private JwtParser parser;

    private String chaveSecreta;
    private long tempoValidoEmMilisegundos;

    @Autowired
    public JwtProvider(@Value("${security.jwt.token.secret-key}") String secretKey,
                       @Value("${security.jwt.token.expiration}")long tempoValidoEmMilisegundos) {

        this.chaveSecreta = Base64.getEncoder().encodeToString(secretKey.getBytes());
        this.tempoValidoEmMilisegundos = tempoValidoEmMilisegundos;
    }

    
    public String criarToken(String username, List<Funcao> funcoes) {
        Claims claims = Jwts.claims().setSubject(username);
        claims.put(FUNCOES_CHAVE, funcoes.stream().map(funcao ->new SimpleGrantedAuthority(funcao.getAuthority()))
                                        .filter(Objects::nonNull)
                                        .collect(Collectors.toList()));
        Date agora = new Date();
        Date expirarEm = new Date(agora.getTime() + tempoValidoEmMilisegundos);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(agora)
                .setExpiration(expirarEm)
                .signWith(SignatureAlgorithm.HS256, chaveSecreta)
                .compact();
    }

  
    public boolean isTokenValido(String token) {
        try {
            Jwts.parser().setSigningKey(chaveSecreta).parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public String getUsername(String token) {
        return Jwts.parser().setSigningKey(chaveSecreta)
                .parseClaimsJws(token).getBody().getSubject();
    }

    public List<GrantedAuthority> getFuncoes(String token) {
        List<Map<String, String>>  funcoesClaims = Jwts.parser().setSigningKey(chaveSecreta)
                .parseClaimsJws(token).getBody().get(FUNCOES_CHAVE, List.class);
        return funcoesClaims.stream().map(funcaoClaim ->
                new SimpleGrantedAuthority(funcaoClaim.get("authority")))
                .collect(Collectors.toList());
    }
}