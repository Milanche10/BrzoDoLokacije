package com.look4.demo.dto.mappers;

import com.look4.demo.dto.SocialPostDTO;
import com.look4.demo.entities.SocialPost;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface SocialPostMapper extends EntityMapper<SocialPostDTO, SocialPost>{
    @Mapping(target = "location", source = "location")
    @Mapping(target = "appUser", source = "appUser")
    @Mapping(target = "images", source = "images")
    @Mapping(target = "likeReactions",source = "likeReactions")
    @Mapping(target = "comments",source = "comments")
    SocialPostDTO toDto(SocialPost s);
}
