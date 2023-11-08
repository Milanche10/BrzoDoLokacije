package com.look4.demo.controllers;

import com.look4.demo.dto.AppUserDTO;
import com.look4.demo.dto.LikeReactionDTO;
import com.look4.demo.dto.SocialPostDTO;
import com.look4.demo.entities.AppUser;
import com.look4.demo.services.LikeReactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/like")
public class LikeReactionController {

    @Autowired
    LikeReactionService likeReactionService;

    @PostMapping("")
    public LikeReactionDTO addLikeReaction(@RequestBody LikeReactionDTO likeReactionDTO){
        return likeReactionService.addLikeReaction(likeReactionDTO);
    }
    @GetMapping("/all")
    public List<LikeReactionDTO> getAllLikeReactions(){
        return likeReactionService.getAllReactions();
    }
    @PostMapping("/user")
    public List<LikeReactionDTO> getAllReactionsByUser(@RequestBody AppUserDTO appUserDTO){
        return likeReactionService.getAllLikesByUser(appUserDTO);
    }
    @PostMapping("/post")
    public List<LikeReactionDTO> getAllLikesOnPost(@RequestBody SocialPostDTO socialPostDTO){
        return likeReactionService.getAllLikesOnPost(socialPostDTO);
    }
    @GetMapping("/count")
    public Long countLikesOnPost(@RequestBody SocialPostDTO socialPostDTO){
        return likeReactionService.countLikesOnPost(socialPostDTO);
    }
    @DeleteMapping("/{likeReactionId}")
    public void deleteLikeReaction(@PathVariable Long likeReactionId){
        likeReactionService.deleteLikeReaction(likeReactionId);
    }

}
