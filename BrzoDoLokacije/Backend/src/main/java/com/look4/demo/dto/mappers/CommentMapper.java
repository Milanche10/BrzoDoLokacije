package com.look4.demo.dto.mappers;

import com.look4.demo.dto.CommentDTO;
import com.look4.demo.dto.CommentSlimDTO;
import com.look4.demo.dto.LikeReactionDTO;
import com.look4.demo.entities.Comment;
import com.look4.demo.entities.LikeReaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommentMapper extends EntityMapper<CommentDTO, Comment>{
    @Mapping(target = "socialPost",source = "socialPost")
    //@Mapping(target = "appUser",source = "appUser")
    CommentDTO toDto(Comment c);

    CommentSlimDTO toSlimDto(Comment c);
}
