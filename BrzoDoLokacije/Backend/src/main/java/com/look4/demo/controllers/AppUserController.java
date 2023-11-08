package com.look4.demo.controllers;

import com.look4.demo.dto.AppUserDTO;
import com.look4.demo.dto.FollowDTO;
import com.look4.demo.entities.AppUser;
import com.look4.demo.services.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/appuser")
public class AppUserController {
    @Autowired
    AppUserService appUserService;


    @GetMapping("/info/{email}")
    public AppUserDTO getUserDetails(@PathVariable String email){
        //String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return appUserService.findByEmail(email);
    }
    @PostMapping("/stat")
    public Map<String,Object> getStat(@RequestBody AppUserDTO appUserDTO){
        return appUserService.GetUserStatistics(appUserDTO);
    }
    @GetMapping("")
    public AppUserDTO findUserByEmail(@RequestParam String email){
        return appUserService.findByEmail(email);
    }
    @GetMapping("/all")
    public List<AppUserDTO> getAllUsers(){
        return appUserService.getAllUsers();
    }

    @PostMapping("/follow/{userId}/{followedUserId}")
    public FollowDTO followUser(@PathVariable Long userId, @PathVariable Long followedUserId){
        return appUserService.follow(userId, followedUserId);
    }
    @PatchMapping("")
    public AppUserDTO updateUser(@RequestBody AppUserDTO appUserDTO){
        return appUserService.updateUser(appUserDTO);
    }
    @PatchMapping("/updatepassword")
    public AppUserDTO updateUserPassword(@RequestBody AppUserDTO appUserDTO){
        return appUserService.updateUserPassword(appUserDTO);
    }
    @DeleteMapping("")
    public void deleteUser(@RequestParam Long userId){
        appUserService.deleteUser(userId);
    }
    @DeleteMapping("/unfollow/{from}/{to}")
    public boolean unfollowUser(@PathVariable Long from,@PathVariable Long to){
        return appUserService.unfollow(from,to);
    }

    @PostMapping("/countfollowing")
    public Long countAllFollowingForUser(@RequestBody AppUserDTO appUserDTO){
        return appUserService.countAllFollowing(appUserDTO);
    }
    @PostMapping("/countfollowers")
    public Long countAllFollowersForUser(@RequestBody AppUserDTO appUserDTO){
        return appUserService.countAllFollowers(appUserDTO);
    }
    @GetMapping("/allfollows")
    public List<FollowDTO> getAllFollows(){
        return appUserService.getAllFollows();
    }
}
