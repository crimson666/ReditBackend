package com.redit.service;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.redit.dto.CommentsDto;
import com.redit.exceptions.PostNotFoundException;
import com.redit.mapper.CommentMapper;
import com.redit.model.*;
import com.redit.repository.CommentRepository;
import com.redit.repository.PostRepository;
import com.redit.repository.UserRepository;

import lombok.AllArgsConstructor;
import java.util.List;
import static java.util.stream.Collectors.toList;
@Service
@AllArgsConstructor
public class CommentService {
	private final PostRepository postRepo;
	private final UserRepository userRepo;
	private final AuthService authService;
	private final CommentMapper commentMapper;
    private final CommentRepository commentRepository;
    private final MailContentBuilder mailContentBuilder;
    private final MailService mailService;
    private static final String POST_URL = "";
    
	public void createComment(CommentsDto commentsDto) {
		Post post = postRepo.findById(commentsDto.getPostId()).orElseThrow(()-> new PostNotFoundException("Post Not Found " + commentsDto.getPostId().toString()));
		Comment comment = commentMapper.map(commentsDto, post, authService.getCurrentUser());
		commentRepository.save(comment);
		String message = mailContentBuilder.build(post.getUser().getUsername() + " posted a comment on your post." + POST_URL);
        sendCommentNotification(message, post.getUser());
	}

	private void sendCommentNotification(String message, User user) {
		   mailService.sendEmail(new NotificationEmail(user.getUsername() + " Commented on your post", user.getEmail(), message));
    }

	public List<CommentsDto> getAllCommentsForPost(Long postId) {
		Post post = postRepo.findById(postId).orElseThrow(() -> new PostNotFoundException(postId.toString()));
		return commentRepository.findByPost(post).stream().map(commentMapper::mapToDto).collect(toList());
	}

	public List<CommentsDto> getAllCommentsForUser(String userName) {
		User user = userRepo.findByUsername(userName).orElseThrow(() -> new UsernameNotFoundException(userName.toString()));
		return commentRepository.findAllByUser(user).stream().map(commentMapper::mapToDto).collect(toList());
	}

}
