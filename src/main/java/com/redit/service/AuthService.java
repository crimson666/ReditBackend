package com.redit.service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import com.redit.dto.AuthenticationResponce;
import com.redit.dto.LoginRequest;
import com.redit.dto.RefreshTokenRequest;
import com.redit.dto.RegisterRequest;
import com.redit.exceptions.SpringRedditException;
import com.redit.model.NotificationEmail;
import com.redit.model.User;
import com.redit.model.VerificationToken;
import com.redit.repository.UserRepository;
import com.redit.repository.VerificationTokenRepository;
import com.redit.secruity.JwtProvider;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@Transactional
public class AuthService {
	private final PasswordEncoder passwordEncoder;//BCrypt Encoding
	private final UserRepository userRepository;
	private final VerificationTokenRepository verificationTokenRepository;
	private final MailService mailService;
	private final AuthenticationManager authenticationManager;
	private final JwtProvider jwtProvider;
	private final RefreshTokenService refreshTokenService;
	
	@Transactional
	public void signup(RegisterRequest registerRequest) {
		User user = new User();
		user.setUsername(registerRequest.getUsername());
		user.setEmail(registerRequest.getEmail());
		user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
		user.setCreated(Instant.now());
		user.setEnabled(false);
		userRepository.save(user);
		String vToken = genrateVerificationToken(user);
		mailService.sendEmail(new NotificationEmail("Please Activate Your Account",
				user.getEmail(),"Thank you for Signing up with my fake Redit," 
		+ "Please click in the beliw link to activate your account,"
		+ "http://localhost:8080/api/auth/accountVerification/"+vToken));
	}
	private String genrateVerificationToken(User user) {
		// TODO Auto-generated method stub
		String vToken = UUID.randomUUID().toString();
		VerificationToken verficationToken = new VerificationToken();
		verficationToken.setToken(vToken);
		verficationToken.setUser(user);
		verificationTokenRepository.save(verficationToken);
		return vToken;
	}
	public void verifyAccount(String vToken) {
		// TODO Auto-generated method stub
		Optional<VerificationToken> verificationToken = verificationTokenRepository.findByToken(vToken);
		verificationToken.orElseThrow(()-> new SpringRedditException("Invalid Token"));
		EnableUser(verificationToken.get());
	}
	
	@Transactional
	private void EnableUser(VerificationToken verificationToken) {
		// TODO Auto-generated method stub
		String username = verificationToken.getUser().getUsername();
		User user = userRepository.findByUsername(username).orElseThrow(()-> new SpringRedditException("User Not Found : " + username));
		user.setEnabled(true);
		userRepository.save(user);	
	}
	public AuthenticationResponce login(LoginRequest loginrequest) {
		// TODO Auto-generated method stub
		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
				loginrequest.getUsername(),loginrequest.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String token = jwtProvider.genarateToken(authentication);
		return AuthenticationResponce.builder()
                .authenticationToken(token)
                .refreshToken(refreshTokenService.generateRefreshToken().getToken())
                .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()))
                .username(loginrequest.getUsername())
                .build();
	}
	 @Transactional(readOnly = true)
    public User getCurrentUser() { 
		 Authentication  principal =  SecurityContextHolder.
                getContext().getAuthentication(); 
        return userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User name not found - " + principal.getName()));
    }
	public AuthenticationResponce refreshToken(@Valid RefreshTokenRequest refreshTokenRequest) {
		refreshTokenService.validateRefreshToken(refreshTokenRequest.getRefreshToken());
		String token = jwtProvider.generateTokenWithUserName(refreshTokenRequest.getUsername());
		return AuthenticationResponce.builder()
                .authenticationToken(token)
                .refreshToken(refreshTokenRequest.getRefreshToken())
                .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()))
                .username(refreshTokenRequest.getUsername())
                .build();
	}
	public boolean isLoggedIn() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return !(authentication instanceof AnonymousAuthenticationToken) && authentication.isAuthenticated();
	}
}
