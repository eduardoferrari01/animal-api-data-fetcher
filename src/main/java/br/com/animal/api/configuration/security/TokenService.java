package br.com.animal.api.configuration.security;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import br.com.animal.api.domain.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class TokenService {

	@Value("${animal.jwt.expiration}")
	private String expiration;
	@Value("${animal.jwt.secret}")
	private String secret;
	
	public String generateToken(Authentication authentication) {
		
		User logged = (User) authentication.getPrincipal();

		Date hoje = new Date();
		
		Date dateExpiracao = new Date(hoje.getTime() + Long.valueOf(this.expiration));
		
		return Jwts.builder()
				.setIssuer("API animal")
				.setSubject(logged.getId())
				.setIssuedAt(hoje)
				.setExpiration(dateExpiracao)
				.signWith(SignatureAlgorithm.HS256, this.secret)
				.compact();
	}
	
	public boolean tokenIsValid(String token) {
		
		try {
			Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token);
	        return true;
	    } catch (Exception e) {
	        return false;
	    }
	}
	
	public String getUserId(String token) {
		
		Claims claims = Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token).getBody();
		
		return  claims.getSubject();
	}
	 
}
