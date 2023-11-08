package com.look4.demo.dto.mappers;


import com.look4.demo.dto.AppUserDTO;
import com.look4.demo.entities.AppUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AppUserMapper extends EntityMapper<AppUserDTO, AppUser> {
    @Mapping(target = "image",source = "image")
    @Mapping(target = "socialPosts",source = "socialPosts")
    @Mapping(target = "likeReactions",source = "likeReactions")
    @Mapping(target = "comments",source = "comments")
    //@Mapping(target = "following",source = "following")
    //@Mapping(target = "followers",source = "followers")
    AppUserDTO toDto(AppUser u);
}
