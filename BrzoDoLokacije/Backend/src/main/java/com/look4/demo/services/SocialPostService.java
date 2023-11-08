package com.look4.demo.services;

import com.look4.demo.dto.AppUserDTO;
import com.look4.demo.dto.ImageDTO;
import com.look4.demo.dto.LocationDTO;
import com.look4.demo.dto.SocialPostDTO;
import com.look4.demo.dto.mappers.AppUserMapper;
import com.look4.demo.dto.mappers.LocationMapper;
import com.look4.demo.dto.mappers.SocialPostMapper;
import com.look4.demo.entities.AppUser;
import com.look4.demo.entities.Location;
import com.look4.demo.entities.SocialPost;
import com.look4.demo.repositories.SocialPostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SocialPostService {
    @Autowired
    SocialPostMapper socialPostMapper;
    @Autowired
    SocialPostRepository socialPostRepository;
    @Autowired
    LocationMapper locationMapper;
    @Autowired
    LikeReactionService likeReactionService;
    @Autowired
    CommentService commentService;
    @Autowired
    AppUserMapper appUserMapper;
    @Autowired
    ImageService imageService;

   /* public SocialPostDTO addImageToSocialPost(SocialPostDTO socialPostDTO, ImageDTO imageDTO, MultipartFile file) throws IOException {
        socialPostDTO = this.addSocialPost(socialPostDTO);
        imageDTO = imageService.uploadPhoto(file);
        Set<ImageDTO> setImages = socialPostDTO.getImages();
        setImages.add(imageDTO);
        socialPostDTO.setImages(setImages);
        return this.updateSocialPost(socialPostDTO);
    }*/
    public SocialPostDTO addSocialPost(SocialPostDTO newSocialPost){
        SocialPost sp = socialPostMapper.toEntity(newSocialPost);
        sp.setDate(Instant.now());
        sp = socialPostRepository.save(sp);
        return socialPostMapper.toDto(sp);
    }

    public String getDatefromSocialPost(Long socialPostId){
        SocialPostDTO socialPostDTO = socialPostRepository.findById(socialPostId).map(socialPostMapper::toDto).get();
        Date myDate = Date.from(socialPostDTO.getDate());
        SimpleDateFormat formatter = new SimpleDateFormat("dd MM yyyy HH:mm:ss");
        String formattedDate = formatter.format(myDate);
        return formattedDate;
    }

    public List<SocialPostDTO> getAllSocialPosts(){
        return socialPostRepository.findAll().stream().map(socialPostMapper::toDto).collect(Collectors.toList());
    }

    public List<SocialPostDTO> getAllSocialPostsSorted(){
        List<SocialPostDTO> allPosts = getAllSocialPosts();
        allPosts.sort(Comparator.comparing(SocialPostDTO::getDate).reversed().thenComparing(new Comparator<SocialPostDTO>() {
            @Override
            public int compare(SocialPostDTO s1, SocialPostDTO s2) {
                int s1Reactions = s1.getLikeReactions().size() + s1.getComments().size();
                int s2Reactions = s2.getLikeReactions().size() + s2.getComments().size();
                return (s1Reactions > s2Reactions ? -1 : 1);
            }
        }));

        return allPosts;
    }

    public List<SocialPostDTO> getTopRatedPosts(){
        List<SocialPostDTO> allPosts = getAllSocialPosts();
        Collections.sort(allPosts,new Comparator<SocialPostDTO>(){
            @Override
            public int compare(SocialPostDTO s1, SocialPostDTO s2){
                int s1Reactions = s1.getLikeReactions().size() + s1.getComments().size();
                int s2Reactions = s2.getLikeReactions().size() + s2.getComments().size();
                return (s1Reactions > s2Reactions ? -1 : 1);
            }
        });
        return allPosts;
    }
    public SocialPostDTO getSocialPostById(Long socialPostId){
        return socialPostMapper.toDto(socialPostRepository.getSocialPostById(socialPostId));
    }
    public Long countPostsByUser(AppUserDTO appUserDTO){
        AppUser appUser = appUserMapper.toEntity(appUserDTO);
        return socialPostRepository.countSocialPostsByAppUser(appUser);
    }

    public List<SocialPostDTO> getAllSocialPostsByLocation(LocationDTO locationDTO){
        Location location = locationMapper.toEntity(locationDTO);
        return socialPostRepository.getSocialPostsByLocation(location).stream().map(socialPostMapper::toDto).collect(Collectors.toList());
    }

    public List<SocialPostDTO> getSocialPostByUser(AppUserDTO appUserDTO){
        AppUser appUser = appUserMapper.toEntity(appUserDTO);
        return socialPostRepository.findSocialPostByAppUser(appUser).stream().map(socialPostMapper::toDto).collect(Collectors.toList());
    }


    public SocialPostDTO updateSocialPost(SocialPostDTO socialPostToBeUpdated){
        return socialPostMapper.toDto(socialPostRepository.save(socialPostMapper.toEntity(socialPostToBeUpdated)));
    }


    public void deleteSocialPost(Long socialPostId){
        boolean exists = socialPostRepository.existsById(socialPostId);
        if(!exists){
            throw new IllegalStateException("SocialPost with id "+socialPostId+" does not exist");
        }
        socialPostRepository.deleteById(socialPostId);
    }





}
