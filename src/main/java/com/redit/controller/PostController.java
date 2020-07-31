package com.redit.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.redit.dto.PostRequest;
import com.redit.dto.PostResponse;
import com.redit.service.PostService;

import lombok.AllArgsConstructor;
import java.util.List;
import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping("/api/posts/")
@AllArgsConstructor
public class PostController {
	private final PostService postService;
	
	@PostMapping
	public ResponseEntity<Void> creatPost(@RequestBody PostRequest postRequest) {
		System.out.println(postRequest);
		postService.save(postRequest);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}
 
	@GetMapping("/{id}")
    public ResponseEntity<PostResponse> getPost(@PathVariable Long id) {
        return status(HttpStatus.OK).body(postService.getPost(id));
    }

	@GetMapping
	public ResponseEntity<List<PostResponse>> getAllPosts() {
		return status(HttpStatus.OK).body(postService.getAllPosts());
	}
	
	@GetMapping("by-subreddit/{id}")
    public ResponseEntity<PostResponse> getPostsBySubreddit(@PathVariable Long id) {
		return status(HttpStatus.OK).body(postService.getPost(id));
    }
	
	@GetMapping("by-user/{name}")
	public ResponseEntity<List<PostResponse>> getPostsByUsername(@PathVariable String name) {
		System.out.println();
	   return status(HttpStatus.OK).body(postService.getPostsByUsername(name));
	}



}
