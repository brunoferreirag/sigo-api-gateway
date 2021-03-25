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
	private static final String URL_USUARIOS = "/api/usuarios";
	private static final String URL_USUARIOS_DELETE = URL_USUARIOS.concat("/{username}");

	private static final String URL_PARADA_PRODUCAO = "/api/erp/parada-producao";
	private static final String URL_PARADA_PRODUCAO_UNICO = URL_PARADA_PRODUCAO.concat("/{id}");

	private static final String URL_STATUS_PRODUCAO = "/api/erp/status-producao";
	
	private static final String URL_NORMA_INDUSTRIAL = "/api/gestao-norma-industrial/norma-industrial";
	private static final String URL_NORMA_INDUSTRIAL_UNICO = URL_NORMA_INDUSTRIAL.concat("/{id}");
	private static final String URL_NORMA_INDUSTRIAL_BUSCA = URL_NORMA_INDUSTRIAL.concat("/busca");

	private static final String ROLE_GPI_PP = "ROLE_SIGO_GPI_PP";
	private static final String ROLE_GPI_SP = "ROLE_SIGO_GPI_SP";
	private static final String ROLE_GNI = "ROLE_SIGO_GN";
	private static final String ROLE_ADMIN_SIGO= "ROLE_ADMIN_SIGO";
	@Autowired
	private DetalheUsuarioService userDetailsService;

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.csrf().disable();

		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().exceptionHandling()
				.authenticationEntryPoint((req, rsp, e) -> rsp.sendError(HttpServletResponse.SC_UNAUTHORIZED)).and()
				.cors()
				.and().authorizeRequests().antMatchers(HttpMethod.POST, URL_USUARIOS + "/login").permitAll()
				.antMatchers(HttpMethod.OPTIONS, URL_USUARIOS + "/login").permitAll()
				.antMatchers(HttpMethod.GET, URL_USUARIOS).hasAuthority(ROLE_ADMIN_SIGO)
				.antMatchers(HttpMethod.OPTIONS, URL_USUARIOS).hasAuthority(ROLE_ADMIN_SIGO)
				.antMatchers(HttpMethod.POST, URL_USUARIOS).hasAuthority(ROLE_ADMIN_SIGO)
				.antMatchers(HttpMethod.OPTIONS, URL_USUARIOS).hasAuthority(ROLE_ADMIN_SIGO)
				.antMatchers(HttpMethod.PUT, URL_USUARIOS).hasAuthority(ROLE_ADMIN_SIGO)
				.antMatchers(HttpMethod.OPTIONS, URL_USUARIOS).hasAuthority(ROLE_ADMIN_SIGO)
				.antMatchers(HttpMethod.DELETE, URL_USUARIOS_DELETE).hasAuthority(ROLE_ADMIN_SIGO)
				.antMatchers(HttpMethod.OPTIONS, URL_USUARIOS_DELETE).hasAuthority(ROLE_ADMIN_SIGO)
				.antMatchers(HttpMethod.GET, URL_PARADA_PRODUCAO).hasAuthority(ROLE_GPI_PP)
				.antMatchers(HttpMethod.OPTIONS, URL_PARADA_PRODUCAO).hasAuthority(ROLE_GPI_PP)
				.antMatchers(HttpMethod.GET, URL_STATUS_PRODUCAO).hasAuthority(ROLE_GPI_SP)
				.antMatchers(HttpMethod.OPTIONS, URL_STATUS_PRODUCAO).hasAuthority(ROLE_GPI_SP)
				.antMatchers(HttpMethod.POST, URL_PARADA_PRODUCAO).hasAuthority(ROLE_GPI_PP)
				.antMatchers(HttpMethod.OPTIONS, URL_PARADA_PRODUCAO).hasAuthority(ROLE_GPI_PP)
				.antMatchers(HttpMethod.GET, URL_PARADA_PRODUCAO_UNICO).hasAuthority(ROLE_GPI_PP)
				.antMatchers(HttpMethod.OPTIONS, URL_PARADA_PRODUCAO_UNICO).hasAuthority(ROLE_GPI_PP)
				.antMatchers(HttpMethod.PUT, URL_PARADA_PRODUCAO_UNICO).hasAuthority(ROLE_GPI_PP)
				.antMatchers(HttpMethod.OPTIONS, URL_PARADA_PRODUCAO_UNICO).hasAuthority(ROLE_GPI_PP)
				.antMatchers(HttpMethod.DELETE, URL_PARADA_PRODUCAO_UNICO).hasAuthority(ROLE_GPI_PP)
				.antMatchers(HttpMethod.OPTIONS, URL_PARADA_PRODUCAO_UNICO).hasAuthority(ROLE_GPI_PP)
				.antMatchers(HttpMethod.GET, URL_NORMA_INDUSTRIAL).hasAuthority(ROLE_GNI)
				.antMatchers(HttpMethod.OPTIONS, URL_NORMA_INDUSTRIAL).hasAuthority(ROLE_GNI)
				.antMatchers(HttpMethod.GET, URL_NORMA_INDUSTRIAL_BUSCA).hasAuthority(ROLE_GNI)
				.antMatchers(HttpMethod.OPTIONS, URL_NORMA_INDUSTRIAL_BUSCA).hasAuthority(ROLE_GNI)
				.antMatchers(HttpMethod.POST, URL_NORMA_INDUSTRIAL).hasAuthority(ROLE_GNI)
				.antMatchers(HttpMethod.OPTIONS, URL_NORMA_INDUSTRIAL).hasAuthority(ROLE_GNI)
				.antMatchers(HttpMethod.PUT, URL_NORMA_INDUSTRIAL_UNICO).hasAuthority(ROLE_GNI)
				.antMatchers(HttpMethod.OPTIONS, URL_NORMA_INDUSTRIAL_UNICO).hasAuthority(ROLE_GNI)
				.antMatchers(HttpMethod.DELETE, URL_NORMA_INDUSTRIAL_UNICO).hasAuthority(ROLE_GNI)
				.antMatchers(HttpMethod.OPTIONS, URL_NORMA_INDUSTRIAL_UNICO).hasAuthority(ROLE_GNI).anyRequest()
				.authenticated();
		
		http.addFilterAfter(new JwtTokenFilter(userDetailsService), UsernamePasswordAuthenticationFilter.class);

	}

	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(Arrays.asList("*"));
		configuration.setAllowedMethods(Arrays.asList("GET", "POST", "OPTIONS", "PUT", "DELETE"));
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