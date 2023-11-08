package com.look4.demo.dto.mappers;

import com.look4.demo.dto.AppUserDTO;
import com.look4.demo.dto.FollowDTO;
import com.look4.demo.entities.AppUser;
import com.look4.demo.entities.Follow;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FollowMapper extends EntityMapper<FollowDTO, Follow>{
    //@Mapping(target = "to",source = "to")
    //@Mapping(target = "from",source = "from")
    FollowDTO toDto(Follow f);
}
