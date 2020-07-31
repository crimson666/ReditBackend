package com.redit.service;

import java.util.List;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;

import com.redit.dto.PostRequest;
import com.redit.dto.PostResponse;
import com.redit.exceptions.PostNotFoundException;
import com.redit.exceptions.SubredditNotFoundException;
import com.redit.mapper.PostMapper;
import com.redit.model.*;
import com.redit.repository.PostRepository;
import com.redit.repository.SubredditRepository;
import com.redit.repository.UserRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class PostService {
	 private final SubredditRepository subredditRepository;
	 private final AuthService authService;
	 private final PostMapper postMapper;
	 private final PostRepository postRepository;
	 private final UserRepository userRepository;
	 
	public void save(PostRequest postRequest) {
		Subreddit subreddit = subredditRepository.findByName(postRequest.getSubredditName())
                .orElseThrow(() -> new SubredditNotFoundException(postRequest.getSubredditName()));
		User CurrentUser = authService.getCurrentUser();
		
		System.out.println();
		System.out.println(postRequest);
		System.out.println(subreddit);
		System.out.println(CurrentUser);
		
		
		postRepository.save(postMapper.map(postRequest, subreddit, CurrentUser));
				
	}
	 @Transactional(readOnly = true)
    public PostResponse getPost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException(id.toString()));
        return postMapper.mapToDto(post);
    }
	 @Transactional(readOnly = true)
    public List<PostResponse> getAllPosts() {
        return postRepository.findAll()
                .stream()
                .map(postMapper::mapToDto)
                .collect(toList());
    }
	@Transactional(readOnly = true)
	public List<PostResponse> getPostsBySubreddit(Long subredditId) {
	    Subreddit subreddit = subredditRepository.findById(subredditId)
	            .orElseThrow(() -> new SubredditNotFoundException(subredditId.toString()));
	    List<Post> posts = postRepository.findAllBySubreddit(subreddit);
	    return posts.stream().map(postMapper::mapToDto).collect(toList());
	}
	@Transactional(readOnly = true)
    public List<PostResponse> getPostsByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        return postRepository.findByUser(user)
                .stream()
                .map(postMapper::mapToDto)
                .collect(toList());
    }

}
