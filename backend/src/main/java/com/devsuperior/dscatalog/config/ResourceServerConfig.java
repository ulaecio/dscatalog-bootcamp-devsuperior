package com.devsuperior.dscatalog.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter{
	
	//CONFIGURAÇÃO DE AUTENTICAÇÃO DE E AUTORIZAÇÃO DE USUÁRIOS
	@Autowired
	private Environment env;
	
	@Autowired
	private JwtTokenStore tokenStore;
	
	private static final String[] PUBLIC = {"/oauth/token", "/h2-console/**"};
	private static final String[] OPERATOR_OR_ADMIM = {"/products/**", "/categories/**"};
	private static final String[] ADMIN = {"/users/**"};
	//CONFIGURAÇÃO DE AUTENTICAÇÃO DE E AUTORIZAÇÃO DE USUÁRIOS
	
	@Override
	public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
		resources.tokenStore(tokenStore);
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {
		
		//PARA LIBERAÇÃO DO TESTE "h2-console"
		if (Arrays.asList(env.getActiveProfiles()).contains("test")) {
			http.headers().frameOptions().disable();
		}
		//PARA LIBERAÇÃO DO TESTE "h2-console"
		
		//CONFIGURAÇÃO DE AUTENTICAÇÃO DE E AUTORIZAÇÃO DE USUÁRIOS
		http.authorizeRequests()
		.antMatchers(PUBLIC).permitAll()
		.antMatchers(HttpMethod.GET, OPERATOR_OR_ADMIM).permitAll()
		.antMatchers(OPERATOR_OR_ADMIM).hasAnyRole("OPERATOR", "ADMIN")
		.antMatchers(ADMIN).hasRole("ADMIN")
		.anyRequest().authenticated();
	}
		//CONFIGURAÇÃO DE AUTENTICAÇÃO DE E AUTORIZAÇÃO DE USUÁRIOS	

}
