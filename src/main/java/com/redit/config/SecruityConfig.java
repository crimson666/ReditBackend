package com.redit.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.redit.secruity.JwtAuthenticationFilter;

import lombok.AllArgsConstructor;

@EnableWebSecurity
@AllArgsConstructor
public class SecruityConfig extends WebSecurityConfigurerAdapter {
	private final UserDetailsService userDetailsService;
	private final JwtAuthenticationFilter jwtAuthenticationFilter;
	
	public void configure(HttpSecurity httpSecruity) throws Exception {
		
		//To Disable csrf attack protection and ant matcher is for 
		//authenticate any request with /api/auth to grant assess
		
		httpSecruity.csrf().disable()
							.authorizeRequests()
							.antMatchers("/api/auth/**")
							.permitAll()
							.antMatchers(HttpMethod.GET,"/api/subredit")
							.permitAll()
							.anyRequest()
							.authenticated();
		httpSecruity.addFilterBefore(jwtAuthenticationFilter,UsernamePasswordAuthenticationFilter.class);
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
	@Bean(name = BeanIds.AUTHENTICATION_MANAGER)
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
}
