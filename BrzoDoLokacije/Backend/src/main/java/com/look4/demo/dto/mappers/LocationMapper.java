package com.look4.demo.dto.mappers;

import com.look4.demo.dto.LocationDTO;
import com.look4.demo.entities.Location;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LocationMapper extends EntityMapper<LocationDTO, Location>{

    LocationDTO toDto(Location l);
}
