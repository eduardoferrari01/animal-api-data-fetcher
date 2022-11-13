package br.com.animal.api.configuration.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import br.com.animal.api.service.UserService;

@EnableWebSecurity
@Configuration
public class SecurityConfigurations  {

	@Autowired
	private AutenticacaoTokenService autenticacaoTokenService;
	@Autowired
	private UserService userService;
	
	@Bean
	public AuthenticationManager authManager(HttpSecurity http, AuthenticationService userDetailService) 
	  throws Exception {
	    return http.getSharedObject(AuthenticationManagerBuilder.class)
	      .userDetailsService(userDetailService)
	      .passwordEncoder(new BCryptPasswordEncoder())
	      .and()
	      .build();
	}
	
	@Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        
		http.authorizeRequests()
	       .antMatchers(HttpMethod.GET ,"/api/animal/information/**").permitAll()
	       .antMatchers(HttpMethod.POST ,"/api/animal/information/**").permitAll()
	       .antMatchers(HttpMethod.POST, "/api/auth/").permitAll()
	       .anyRequest().authenticated()
	       .and().csrf().disable().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
	       .addFilterBefore(new AuthenticationViaTokenFilter(autenticacaoTokenService, userService), UsernamePasswordAuthenticationFilter.class);

		return http.build();
    }
}
