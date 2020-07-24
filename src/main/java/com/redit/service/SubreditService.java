package com.redit.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.redit.dto.SubreditDto;
import com.redit.exceptions.SpringRedditException;
import com.redit.mapper.SubreditMapper;
import com.redit.model.Subreddit;
import com.redit.repository.SubredditRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
@Slf4j
public class SubreditService {
	private final SubredditRepository subredditRepository;
	private final SubreditMapper subreditMapper;
	
	@Transactional
	public SubreditDto save(SubreditDto SubreditDto) {
		
		Subreddit save = subredditRepository.save(subreditMapper.mapDtoToSubreddit(SubreditDto));
		SubreditDto.setId(save.getId());
		return SubreditDto;
	}

//	private Subreddit mapSubreditDto(SubreditDto subreditDto) {
//		return Subreddit.builder().name(subreditDto.getName())
//				.description(subreditDto.getDescription())
//				.build();
//	}
	@Transactional(readOnly = true)
	public List<SubreditDto> getAll() {
		return subredditRepository.findAll().stream()
				.map(subreditMapper::mapSubredditToDto)
				.collect(toList());
	}
//	private SubreditDto mapToDto(Subreddit subreddit) {
//		return SubreditDto.builder().name(subreddit.getName())
//				.id(subreddit.getId())
//				.numberOfPost(subreddit.getPosts().size())
//				.build();
//	}
	public SubreditDto getSubredit(Long id) {
		Subreddit subreddit = subredditRepository.findById(id).orElseThrow(()-> new SpringRedditException("No Such Redit Post Found"));
		return subreditMapper.mapSubredditToDto(subreddit);
	}

}
