package com.look4.demo.services;

import com.look4.demo.dto.LocationDTO;
import com.look4.demo.dto.mappers.LocationMapper;
import com.look4.demo.entities.Location;
import com.look4.demo.repositories.LocationRepository;
import liquibase.repackaged.org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LocationService {
    @Autowired
    LocationRepository locationRepository;
    @Autowired
    LocationMapper locationMapper;
    public LocationDTO addToDataBase(LocationDTO location){
        Location l = locationMapper.toEntity(location);
        l = locationRepository.save(l);
        return locationMapper.toDto(l);
    }
    public LocationDTO addLocation(LocationDTO location){
        List<LocationDTO> allLocations = this.getAllLocations();
        if(allLocations== null){
            return addToDataBase(location);
        }
        for(LocationDTO locationDTO : allLocations) {
            //System.out.println("Izmedju"+locationDTO.getName()+"  "+location.getName());
            //System.out.println(getDistanceFromLatLonInKm(location.getLat(),location.getLon(),locationDTO.getLat(),locationDTO.getLon()));
            //System.out.println(distance(location.getLat(),location.getLon(),locationDTO.getLat(),locationDTO.getLon()));
            if(distance(location.getLat(),location.getLon(),locationDTO.getLat(),locationDTO.getLon())<5){
                return null;
            }
        }
        return addToDataBase(location);
    }
    public double distance(double lat1,double lon1,double lat2,double lon2) {
        double p = 0.017453292519943295;    // Math.PI / 180
        double a = 0.5 - Math.cos((lat2 - lat1) * p)/2 +
                Math.cos(lat1 * p) * Math.cos(lat2 * p) *
                        (1 - Math.cos((lon2 - lon1) * p))/2;

        return 12742 * Math.asin(Math.sqrt(a)); // 2 * R; R = 6371 km
    }
    public double getDistanceFromLatLonInKm(double lat1,double lon1,double lat2, double lon2){
        double R = 6371;
        double dLat = deg2rad(lat2-lat1);
        double dLon = deg2rad(lon2-lon1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) *
                        Math.sin(dLon/2) * Math.sin(dLon/2);
        double c = 2* Math.atan2(Math.sqrt(a),Math.sqrt(1-a));
        double d = R*c;
        return d;
    }
    public double deg2rad(double deg){
        return deg * (Math.PI*180);
    }


    public List<LocationDTO> getAllLocations(){
        return locationRepository.findAll().stream().map(locationMapper::toDto).collect(Collectors.toList());
    }


    public LocationDTO getLocationByCordinates(double lon, double lat){
        return locationMapper.toDto(locationRepository.findLocationByLonAndLat(lon,lat));
    }


    public LocationDTO updateLocation(LocationDTO locationToBeUpdated){
        return locationMapper.toDto(locationRepository.save(locationMapper.toEntity(locationToBeUpdated)));
    }

    public void deleteLocation(Long locationId){
        boolean exists = locationRepository.existsById(locationId);
        if(!exists){
            throw new IllegalStateException("Location with id "+locationId+" does not exist");
        }
        locationRepository.deleteById(locationId);
    }



}
