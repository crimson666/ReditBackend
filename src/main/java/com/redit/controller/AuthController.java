package com.redit.controller;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.redit.dto.AuthenticationResponce;
import com.redit.dto.LoginRequest;
import com.redit.dto.RefreshTokenRequest;
import com.redit.dto.RegisterRequest;
import com.redit.service.AuthService;
import com.redit.service.RefreshTokenService;

import lombok.AllArgsConstructor;

import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {
	private final AuthService authService;
	private final RefreshTokenService refreshTokenService;
	@PostMapping("/signup")
	public ResponseEntity<String> signup(@RequestBody RegisterRequest registerRequest) {
		authService.signup(registerRequest);
		return new ResponseEntity<>("User Registration Successful", HttpStatus.OK);
	}
	@GetMapping("accountVerification/{token}")
	public ResponseEntity<String> verifyAccount(@PathVariable String token){
		authService.verifyAccount(token);
		return new ResponseEntity<>("User Activated Successfully", HttpStatus.OK);
	}
	@PostMapping("/login")
	public AuthenticationResponce login(@RequestBody LoginRequest loginrequest) {
		return authService.login(loginrequest);
	}
	@PostMapping("refresh/token")
	public AuthenticationResponce refreshTokens(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        return authService.refreshToken(refreshTokenRequest);
    }
	@PostMapping("/logout")
	public ResponseEntity<String> logOut(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
		refreshTokenService.deleteRefreshToken(refreshTokenRequest.getRefreshToken());
		return ResponseEntity.status(HttpStatus.OK).body("Refresh Token Deleted Successfully");
	}
	
	
}
