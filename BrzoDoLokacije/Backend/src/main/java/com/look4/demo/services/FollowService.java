package com.look4.demo.services;

import com.look4.demo.dto.AppUserDTO;
import com.look4.demo.dto.FollowDTO;
import com.look4.demo.dto.LocationDTO;
import com.look4.demo.dto.mappers.AppUserMapper;
import com.look4.demo.dto.mappers.FollowMapper;
import com.look4.demo.entities.AppUser;
import com.look4.demo.entities.Follow;
import com.look4.demo.repositories.FollowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FollowService {
    @Autowired
    FollowMapper followMapper;
    @Autowired
    FollowRepository followRepository;
    @Autowired
    AppUserMapper appUserMapper;

    public FollowDTO addToDataBase(FollowDTO followDTO){
        Follow follow = followMapper.toEntity(followDTO);
        follow = followRepository.save(follow);
        return followMapper.toDto(follow);
    }
    public FollowDTO addFollow(FollowDTO followDTO){
        boolean exists = followRepository.existsByFromAndTo(followMapper.toEntity(followDTO).getFrom(),followMapper.toEntity(followDTO).getTo());
        if(exists)
        {
            throw new IllegalStateException("Vec pratite ovog usera");
        }
        else
        {
            return addToDataBase(followDTO);
        }

    }

    //Count all following grouped by From
    public Long countAllFollowing(AppUserDTO appUserDTO){
        return followRepository.countAllByFrom(appUserMapper.toEntity(appUserDTO));
    }
    //Count all followers grouped by to
    public Long countAllFollowers(AppUserDTO appUserDTO){
        return followRepository.countAllByTo(appUserMapper.toEntity(appUserDTO));
    }

    public boolean deleteFollow(AppUserDTO from, AppUserDTO to ){
        AppUser fromUser = appUserMapper.toEntity(from);
        AppUser toUser = appUserMapper.toEntity(to);
        boolean exists = followRepository.existsByFromAndTo(fromUser, toUser);
        if(!exists){
            throw new IllegalStateException("Following with from "+from.getFirstName()+"to"+to.getFirstName()+" does not exist");
        }
        FollowDTO followDTO = followMapper.toDto(followRepository.findByFromAndTo(fromUser,toUser));
        System.out.println(followDTO.getId());
        followRepository.deleteById(followDTO.getId());

        return true;
    }
    public List<FollowDTO> getAllFollows(){
        return followRepository.findAll().stream().map(followMapper::toDto).collect(Collectors.toList());
    }

}
