package com.look4.demo.controllers;

import com.look4.demo.dto.AppUserDTO;
import com.look4.demo.dto.LocationDTO;
import com.look4.demo.dto.SocialPostDTO;
import com.look4.demo.entities.SocialPost;
import com.look4.demo.services.SocialPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController()
@RequestMapping("/api/socialpost")
public class SocialPostController {
    @Autowired
    SocialPostService socialPostService;

    @GetMapping("/all")
    public List<SocialPostDTO> getAllSocialPosts(){
        return socialPostService.getAllSocialPosts();
    }
    @GetMapping("/allsorted")
    public List<SocialPostDTO> getAllSocialPostsSorted(){
        return socialPostService.getAllSocialPostsSorted();
    }
    @GetMapping("/toprated")
    public List<SocialPostDTO> getTopRatedPosts(){
        return socialPostService.getTopRatedPosts();
    }

    @PostMapping("/userposts")
    public List<SocialPostDTO> getAllPostsByUser(@RequestBody AppUserDTO user){
        return socialPostService.getSocialPostByUser(user);
    }

    @GetMapping("/count")
    public Long countPostsByUser(@RequestBody AppUserDTO appUserDTO){
        return socialPostService.countPostsByUser(appUserDTO);
    }
    @PostMapping("/location")
    public  List<SocialPostDTO> getAllSocialPostByLocation(@RequestBody LocationDTO locationDTO){
        return socialPostService.getAllSocialPostsByLocation(locationDTO);
    }
    @GetMapping("/post/{socialPostId}")
    public SocialPostDTO getSocialPostById(@PathVariable Long socialPostId){
        return socialPostService.getSocialPostById(socialPostId);
    }

    @PostMapping("")
    public SocialPostDTO addSocialPost(@RequestBody SocialPostDTO newSocialPost){
        return socialPostService.addSocialPost(newSocialPost);
    }

    @PatchMapping("")
    public SocialPostDTO updateSocialPost(@RequestBody SocialPostDTO socialPostToBeUpdated){
        return socialPostService.updateSocialPost(socialPostToBeUpdated);
    }
    @GetMapping("/date/{socialPostId}")
    public String getDatefromSocialPost(@PathVariable Long socialPostId){
        return socialPostService.getDatefromSocialPost(socialPostId);
    }


    @DeleteMapping("")
    public void deleteSocialPost(@RequestParam Long socialPostId){
        socialPostService.deleteSocialPost(socialPostId);
    }
}
