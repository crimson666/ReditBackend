package com.redit.service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.redit.dto.RegisterRequest;
import com.redit.exceptions.SpringRedditException;
import com.redit.model.NotificationEmail;
import com.redit.model.User;
import com.redit.model.VerificationToken;
import com.redit.repository.UserRepository;
import com.redit.repository.VerificationTokenRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AuthService {
	//@Autowired//Its good Practice to do in Constractor
	private final PasswordEncoder passwordEncoder;//BCrypt Encoding
	private final UserRepository userRepository;
	private final VerificationTokenRepository verificationTokenRepository;
	private final MailService mailService;
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
	private void EnableUser(VerificationToken verificationToken) {
		// TODO Auto-generated method stub
		String username = verificationToken.getUser().getUsername();
		User user = userRepository.findByUsername(username).orElseThrow(()-> new SpringRedditException("User Not Found : " + username));
		user.setEnabled(true);
		userRepository.save(user);
	}
}
