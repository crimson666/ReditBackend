package com.redit.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.redit.dto.VoteDto;
import com.redit.exceptions.PostNotFoundException;
import com.redit.exceptions.SpringRedditException;
import com.redit.model.*;
import com.redit.repository.PostRepository;
import com.redit.repository.VoteRepository;
import static  com.redit.model.VoteType.UPVOTE;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class VoteService {

	private final VoteRepository voteRepository;
    private final PostRepository postRepository;
    private final AuthService authService;
    @Transactional
	public void vote(VoteDto voteDto) {
    	Post post = postRepository.findById(voteDto.getPostId())
                .orElseThrow(() -> new PostNotFoundException("Post Not Found with ID - " + voteDto.getPostId()));
    	//Finding the Latest vote by the user according to the specific user and post
    	Optional<Vote> voteByPostAndUser = voteRepository.findTopByPostAndUserOrderByVoteIdDesc(post, authService.getCurrentUser());
    	if (voteByPostAndUser.isPresent() && voteByPostAndUser.get().getVoteType().equals(voteDto.getVoteType())) {
            throw new SpringRedditException("You have already " + voteDto.getVoteType() + "'d for this post");
        }
    	//Still bit Sketchy in the mechanish
    	if (UPVOTE.equals(voteDto.getVoteType())) {
            post.setVoteCount(post.getVoteCount() + 1);
        } else {
            post.setVoteCount(post.getVoteCount() - 1);
        }
    	voteRepository.save(mapToVote(voteDto, post));
        postRepository.save(post);
		
	}
	private Vote mapToVote(VoteDto voteDto, Post post) {
		return Vote.builder().voteType(voteDto.getVoteType()).post(post).user(authService.getCurrentUser()).build();
	}
	
}
