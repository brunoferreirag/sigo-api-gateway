package br.com.indtextbr.services.apigateway.config;

import java.util.Arrays;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import br.com.indtextbr.services.apigateway.seguranca.DetalheUsuarioService;
import br.com.indtextbr.services.apigateway.seguranca.JwtTokenFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {
	private static final String URL_USUARIOS = "/sigo/api/v1/usuarios";
	private static final String URL_USUARIOS_DELETE =URL_USUARIOS.concat("/{username}");
	private static final String URL_SETOR_INDUSTRIAL = "/sigo/api/erp/v1/setores-industriais";
	private static final String URL_SETOR_INDUSTRIAL_UNICO = URL_SETOR_INDUSTRIAL.concat("/{URL_SETOR_INDUSTRIAL}");
	
	private static final String URL_ARMAZENS = "/sigo/api/logistica/v1/armazens";
	private static final String URL_ARMAZENS_UNICO = URL_ARMAZENS.concat("/{id}");
	
	private static final String URL_NORMA_INDUSTRIAL = "/sigo/api/norma-industrial/v1/";
	private static final String URL_NORMA_INDUSTRIAL_UNICO = URL_NORMA_INDUSTRIAL.concat("/{id}");
	private static final String URL_NORMA_INDUSTRIAL_BUSCA =URL_NORMA_INDUSTRIAL.concat("/busca");
	
	private static final String ROLE_GPI = "ROLE_SIGO_GPI";
	private static final String ROLE_GNI = "ROLE_SIGO_GN";
	@Autowired
	private DetalheUsuarioService userDetailsService;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors().and().authorizeRequests().antMatchers("**").permitAll();
		http.csrf().disable();

		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
        .exceptionHandling().authenticationEntryPoint((req, rsp, e) -> rsp.sendError(HttpServletResponse.SC_UNAUTHORIZED))
        .and();

		http.addFilterAfter(new JwtTokenFilter(userDetailsService), UsernamePasswordAuthenticationFilter.class);
		/*
		http.authorizeRequests().antMatchers(URL_USUARIOS + "/login/**").permitAll()
		         .antMatchers(HttpMethod.GET, URL_USUARIOS).hasAuthority("ROLE_ADMIN_SIGO")
		         .antMatchers(HttpMethod.OPTIONS, URL_USUARIOS).hasAuthority("ROLE_ADMIN_SIGO")
		         .antMatchers(HttpMethod.POST, URL_USUARIOS).hasAuthority("ROLE_ADMIN_SIGO")
		         .antMatchers(HttpMethod.OPTIONS, URL_USUARIOS).hasAuthority("ROLE_ADMIN_SIGO")
		         .antMatchers(HttpMethod.PUT, URL_USUARIOS).hasAuthority("ROLE_ADMIN_SIGO")
		         .antMatchers(HttpMethod.OPTIONS, URL_USUARIOS).hasAuthority("ROLE_ADMIN_SIGO")
		         .antMatchers(HttpMethod.DELETE, URL_USUARIOS_DELETE).hasAuthority("ROLE_ADMIN_SIGO")
		         .antMatchers(HttpMethod.OPTIONS, URL_USUARIOS_DELETE).hasAuthority("ROLE_ADMIN_SIGO")
		         .antMatchers(HttpMethod.GET,URL_SETOR_INDUSTRIAL).hasAuthority(ROLE_GPI)
		         .antMatchers(HttpMethod.OPTIONS,URL_SETOR_INDUSTRIAL).hasAuthority(ROLE_GPI)
		         .antMatchers(HttpMethod.POST,URL_SETOR_INDUSTRIAL).hasAuthority(ROLE_GPI)
		         .antMatchers(HttpMethod.PUT,URL_SETOR_INDUSTRIAL_UNICO).hasAuthority(ROLE_GPI)
		         .antMatchers(HttpMethod.OPTIONS,URL_SETOR_INDUSTRIAL_UNICO).hasAuthority(ROLE_GPI)
		         .antMatchers(HttpMethod.DELETE,URL_SETOR_INDUSTRIAL_UNICO).hasAuthority(ROLE_GPI)
		         .antMatchers(HttpMethod.OPTIONS,URL_SETOR_INDUSTRIAL_UNICO).hasAuthority(ROLE_GPI)
		         .antMatchers(HttpMethod.GET,URL_SETOR_INDUSTRIAL_UNICO).hasAuthority(ROLE_GPI)
		         .antMatchers(HttpMethod.OPTIONS,URL_SETOR_INDUSTRIAL_UNICO).hasAuthority(ROLE_GPI)
		         .antMatchers(HttpMethod.GET,URL_ARMAZENS).hasAuthority(ROLE_GPI)
		         .antMatchers(HttpMethod.OPTIONS,URL_ARMAZENS).hasAuthority(ROLE_GPI)
		         .antMatchers(HttpMethod.POST,URL_ARMAZENS).hasAuthority(ROLE_GPI)
		         .antMatchers(HttpMethod.OPTIONS,URL_ARMAZENS).hasAuthority(ROLE_GPI)
		         .antMatchers(HttpMethod.GET,URL_ARMAZENS_UNICO).hasAuthority(ROLE_GPI)
		         .antMatchers(HttpMethod.OPTIONS,URL_ARMAZENS_UNICO).hasAuthority(ROLE_GPI)
		         .antMatchers(HttpMethod.PUT,URL_ARMAZENS_UNICO).hasAuthority(ROLE_GPI)
		         .antMatchers(HttpMethod.OPTIONS,URL_ARMAZENS_UNICO).hasAuthority(ROLE_GPI)
		         .antMatchers(HttpMethod.DELETE,URL_ARMAZENS_UNICO).hasAuthority(ROLE_GPI)
		         .antMatchers(HttpMethod.OPTIONS,URL_ARMAZENS_UNICO).hasAuthority(ROLE_GPI)
		         .antMatchers(HttpMethod.GET,URL_NORMA_INDUSTRIAL).hasAuthority(ROLE_GNI)
		         .antMatchers(HttpMethod.OPTIONS,URL_NORMA_INDUSTRIAL).hasAuthority(ROLE_GNI)
		         .antMatchers(HttpMethod.GET,URL_NORMA_INDUSTRIAL_BUSCA).hasAuthority(ROLE_GNI)
		         .antMatchers(HttpMethod.OPTIONS,URL_NORMA_INDUSTRIAL_BUSCA).hasAuthority(ROLE_GNI)
		         .antMatchers(HttpMethod.POST,URL_NORMA_INDUSTRIAL).hasAuthority(ROLE_GNI)
		         .antMatchers(HttpMethod.OPTIONS,URL_NORMA_INDUSTRIAL).hasAuthority(ROLE_GNI)
		         .antMatchers(HttpMethod.PUT,URL_NORMA_INDUSTRIAL_UNICO).hasAuthority(ROLE_GNI)
		         .antMatchers(HttpMethod.OPTIONS,URL_NORMA_INDUSTRIAL_UNICO).hasAuthority(ROLE_GNI)
		         .antMatchers(HttpMethod.DELETE,URL_NORMA_INDUSTRIAL_UNICO).hasAuthority(ROLE_GNI)
		         .antMatchers(HttpMethod.OPTIONS,URL_NORMA_INDUSTRIAL_UNICO).hasAuthority(ROLE_GNI)  
		         .antMatchers("**")
				.authenticated();
		
		*/
	}
	
	 @Bean
	  CorsConfigurationSource corsConfigurationSource() {
	      CorsConfiguration configuration = new CorsConfiguration();
	      configuration.setAllowedOrigins(Arrays.asList("*"));
	      configuration.setAllowedMethods(Arrays.asList("GET","POST","OPTIONS","PUT","DELETE"));
	      UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	      source.registerCorsConfiguration("/**", configuration);
	      return source;
	  }
	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(12);
	}

}