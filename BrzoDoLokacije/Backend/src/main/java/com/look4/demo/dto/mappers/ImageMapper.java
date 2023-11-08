package com.look4.demo.dto.mappers;

import com.look4.demo.dto.ImageDTO;
import com.look4.demo.dto.ImageSlimDTO;
import com.look4.demo.entities.Image;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ImageMapper extends EntityMapper<ImageDTO, Image>{
    @Mapping(target = "socialPost",source = "socialPost",ignore = true)
    //@Mapping(target = "appUser",source = "appUser",ignore = true)
    ImageDTO toDto(Image i);

    ImageSlimDTO toSlimDto(Image i);
}
