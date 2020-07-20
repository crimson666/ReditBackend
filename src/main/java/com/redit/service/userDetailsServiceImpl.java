package com.redit.service;

import java.util.Collection;
import java.util.Optional;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.redit.model.User;
import com.redit.repository.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class userDetailsServiceImpl implements UserDetailsService{
	private final UserRepository userRepository;

	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> userOptional = userRepository.findByUsername(username);
		User user= userOptional.orElseThrow(()-> new UsernameNotFoundException("No user found :" + username ));
		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),user.isEnabled(),true,true,true,getAuthority("USER"));
	}

	private Collection<? extends GrantedAuthority> getAuthority(String role) {
		return singletonList(new SimpleGrantedAuthority(role));
	}

}
