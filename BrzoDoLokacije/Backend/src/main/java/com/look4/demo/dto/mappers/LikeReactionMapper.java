package com.look4.demo.dto.mappers;

import com.look4.demo.dto.LikeReactionDTO;
import com.look4.demo.dto.LikeReactionSlimDTO;
import com.look4.demo.entities.LikeReaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LikeReactionMapper extends EntityMapper<LikeReactionDTO, LikeReaction>{
    @Mapping(target = "socialPost",source = "socialPost")
    @Mapping(target = "appUser",source = "appUser")
    LikeReactionDTO toDto(LikeReaction l);

    LikeReactionSlimDTO toSlimDto(LikeReaction l);
}
