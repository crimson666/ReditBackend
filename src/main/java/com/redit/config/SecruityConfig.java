package com.redit.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.AllArgsConstructor;

@EnableWebSecurity
@AllArgsConstructor
public class SecruityConfig extends WebSecurityConfigurerAdapter {
	private final UserDetailsService userDetailsService;
	
	public void configure(HttpSecurity httpSecruity) throws Exception {
		//To Disable csrf attack protection and ant matcher is for 
		//authenticate any request with /api/auth to grant assess
		httpSecruity.csrf().disable()
							.authorizeRequests()
							.antMatchers("/api/auth/**")
							.permitAll()
							.anyRequest()
							.authenticated();
	}
	@Autowired
	public void confuigerGlobal(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
		authenticationManagerBuilder.userDetailsService(userDetailsService)
									.passwordEncoder(passwordEncoder());
	}
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
