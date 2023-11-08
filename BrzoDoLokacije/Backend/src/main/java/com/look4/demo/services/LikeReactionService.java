package com.look4.demo.services;

import com.look4.demo.dto.AppUserDTO;
import com.look4.demo.dto.LikeReactionDTO;
import com.look4.demo.dto.SocialPostDTO;
import com.look4.demo.dto.mappers.AppUserMapper;
import com.look4.demo.dto.mappers.LikeReactionMapper;
import com.look4.demo.dto.mappers.SocialPostMapper;
import com.look4.demo.entities.AppUser;
import com.look4.demo.entities.LikeReaction;
import com.look4.demo.entities.SocialPost;
import com.look4.demo.repositories.LikeReactionRepository;
import liquibase.pro.packaged.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LikeReactionService {
    @Autowired
    LikeReactionMapper likeReactionMapper;
    @Autowired
    LikeReactionRepository likeReactionRepository;
    @Autowired
    AppUserMapper appUserMapper;
    @Autowired
    SocialPostMapper socialPostMapper;

    public LikeReactionDTO addLikeReaction(LikeReactionDTO likeReactionDTO){
        LikeReaction like = likeReactionMapper.toEntity(likeReactionDTO);
        like = likeReactionRepository.save(like);
        return likeReactionMapper.toDto(like);
    }

    public Long countLikesOnPost(SocialPostDTO socialPostDTO){
        SocialPost socialPost = socialPostMapper.toEntity(socialPostDTO);
        return likeReactionRepository.countLikeReactionBySocialPost(socialPost);
    }

    public List<LikeReactionDTO> getAllReactions(){
        return likeReactionRepository.findAll().stream().map(likeReactionMapper::toDto).collect(Collectors.toList());
    }

    public List<LikeReactionDTO> getAllLikesByUser(AppUserDTO appUserDTO){
        AppUser appUser = appUserMapper.toEntity(appUserDTO);
        return likeReactionRepository.getLikeReactionsByAppUser(appUser).stream().map(likeReactionMapper::toDto).collect(Collectors.toList());
    }

    public List<LikeReactionDTO> getAllLikesOnPost(SocialPostDTO socialPostDTO){
        SocialPost socialPost = socialPostMapper.toEntity(socialPostDTO);
        return likeReactionRepository.getLikeReactionsBySocialPost(socialPost).stream().map(likeReactionMapper::toDto).collect(Collectors.toList());
    }

    public void deleteLikeReaction(Long likeReactionId){
        boolean exists = likeReactionRepository.existsById(likeReactionId);
        if(!exists){
            throw new IllegalStateException("Like reaction with id "+likeReactionId+" does not exist");
        }
        likeReactionRepository.deleteById(likeReactionId);
    }
}
