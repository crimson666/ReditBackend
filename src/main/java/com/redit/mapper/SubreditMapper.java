package com.redit.mapper;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.redit.dto.SubreditDto;
import com.redit.model.Subreddit;
import com.redit.model.Post;
import java.util.List;

@Mapper(componentModel = "spring")
public interface SubreditMapper {

    @Mapping(target = "numberOfPost", expression = "java(mapPosts(subreddit.getPosts()))")
    SubreditDto mapSubredditToDto(Subreddit subreddit);

    default Integer mapPosts(List<Post> numberOfPosts) {
        return numberOfPosts.size();
    }

    @InheritInverseConfiguration
    @Mapping(target = "posts", ignore = true)
    Subreddit mapDtoToSubreddit(SubreditDto subredditDto);
}
