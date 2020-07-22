package com.redit.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.redit.dto.SubreditDto;
import com.redit.service.SubreditService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/subredit")
@AllArgsConstructor
@Slf4j
public class SubReditController {
	private final SubreditService subreditService;
	@PostMapping
	public ResponseEntity<SubreditDto> createSubredit(@RequestBody SubreditDto subreditDto) {
		return ResponseEntity.status(HttpStatus.CREATED).body(subreditService.save(subreditDto));
	}
	@GetMapping
	public ResponseEntity<List<SubreditDto>> getAllSubredit() {
		return ResponseEntity.status(HttpStatus.OK).body(subreditService.getAll());
	}
}