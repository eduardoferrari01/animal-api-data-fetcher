package br.com.animal.api.configuration.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import br.com.animal.api.domain.User;
import br.com.animal.api.service.UserService;

public class AuthenticationViaTokenFilter extends OncePerRequestFilter {

	private AutenticacaoTokenService autenticacaoService;
	private UserService userService;
	
	public AuthenticationViaTokenFilter(AutenticacaoTokenService autenticacaoService, UserService mockDB) {
		 this.autenticacaoService = autenticacaoService;
		 this.userService = mockDB;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String token = recoverToken(request);
		boolean isValid = autenticacaoService.tokenIsValid(token);
		
		if(isValid)
			authenticateClient(token);	

		filterChain.doFilter(request, response);
	}

	private void authenticateClient(String token) {
		
		String userId = autenticacaoService.getUserId(token);
		
		User user = userService.findById(userId);
		
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
		
		SecurityContextHolder.getContext().setAuthentication(authenticationToken);
		
	}

	public AutenticacaoTokenService getAutenticacaoService() {
		return autenticacaoService;
	}

	public void setAutenticacaoService(AutenticacaoTokenService autenticacaoService) {
		this.autenticacaoService = autenticacaoService;
	}

	private String recoverToken(HttpServletRequest request) {

		String token = request.getHeader("Authorization");

		if (token == null || token.isEmpty() || !token.startsWith("Bearer ")) {
			return null;
		}

		return token.substring(7, token.length());
	}
}
