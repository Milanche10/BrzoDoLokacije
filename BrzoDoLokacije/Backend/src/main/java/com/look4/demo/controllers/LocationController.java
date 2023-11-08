package com.look4.demo.controllers;

import com.look4.demo.dto.LocationDTO;
import com.look4.demo.dto.mappers.LocationMapper;
import com.look4.demo.services.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("/api/location")
public class LocationController {

    @Autowired
    LocationService locationService;
    @GetMapping("/all")
    public List<LocationDTO> getAllLocations(){
        return locationService.getAllLocations();
    }

    @PostMapping("")
    public LocationDTO addLocation(@RequestBody LocationDTO location){
        return locationService.addLocation(location);
    }

    @GetMapping("")
    public LocationDTO getLocationByCordinates(@RequestParam double lon,@RequestParam double lat){
        return locationService.getLocationByCordinates(lon,lat);
    }

    @PatchMapping("")
    public LocationDTO updateLocation(@RequestBody LocationDTO locationToBeUpdated){
        return locationService.updateLocation(locationToBeUpdated);
    }

    @DeleteMapping("/{locationId}")
    public void deleteLocation(@PathVariable Long locationId){
        locationService.deleteLocation(locationId);
    }


}
