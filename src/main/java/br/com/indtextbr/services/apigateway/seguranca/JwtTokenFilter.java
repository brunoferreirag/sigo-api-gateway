package br.com.indtextbr.services.apigateway.seguranca;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.filter.OncePerRequestFilter;

import lombok.extern.log4j.Log4j2;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Optional;

@Log4j2
public class JwtTokenFilter extends OncePerRequestFilter {
	private static final Logger LOGGER = LoggerFactory.getLogger(JwtTokenFilter.class);
	private static final String BEARER = "Bearer";

	private DetalheUsuarioService userDetailsService;

	public JwtTokenFilter(DetalheUsuarioService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}

	private Optional<String> getBearerToken(String headerVal) {
		if (headerVal != null && headerVal.startsWith(BEARER)) {
			return Optional.of(headerVal.replace(BEARER, "").trim());
		}
		return Optional.empty();
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String headerValue = request.getHeader("Authorization");
		getBearerToken(headerValue).ifPresent(token -> {
			// Pull the Username and Roles from the JWT to construct the user details
			userDetailsService.loadUserByJwtToken(token).ifPresent(userDetails -> {
				// Add the user details (Permissions) to the Context for just this API
				// invocation
				SecurityContextHolder.getContext().setAuthentication(
						new PreAuthenticatedAuthenticationToken(userDetails, "", userDetails.getAuthorities()));
			});
		});

		filterChain.doFilter(request, response);
	}
}