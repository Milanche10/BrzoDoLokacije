package com.look4.demo.services;


import com.look4.demo.dto.*;
import com.look4.demo.dto.mappers.AppUserMapper;
import com.look4.demo.dto.mappers.LocationMapper;
import com.look4.demo.entities.AppUser;
import com.look4.demo.entities.SocialPost;
import com.look4.demo.repositories.AppUserRepository;
import com.look4.demo.security.JwtUtil;
import com.look4.demo.security.PasswordEncoder;
import liquibase.pro.packaged.F;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class AppUserService {

    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AppUserRepository appUserRepository;
    @Autowired
    AppUserMapper appUserMapper;
    @Lazy
    @Autowired
    SocialPostService socialPostService;
    @Autowired
    LocationService locationService;
    @Autowired
    LocationMapper locationMapper;
    @Autowired
    LikeReactionService likeReactionService;
    @Autowired
    CommentService commentService;

    @Autowired
    FollowService followService;


    public AppUserDTO addUser(AppUserDTO user){
        AppUser u = appUserMapper.toEntity(user);
        u= appUserRepository.save(u);
        return appUserMapper.toDto(u);
    }
    public List<AppUserDTO> getAllUsers(){
        return appUserRepository.findAll().stream().map(appUserMapper::toDto).collect(Collectors.toList());
    }
    public Map<String,Object> GetUserStatistics(AppUserDTO appUserDTO){
        int numberOfPosts = Math.toIntExact(socialPostService.countPostsByUser(appUserDTO));
        List<SocialPostDTO> socialPostDTOS = socialPostService.getSocialPostByUser(appUserDTO);
        Set<String> locations = new HashSet<>();
        int numberOfLikes = 0;
        int numberOfComments = 0;
        for(SocialPostDTO socialPostDTO : socialPostDTOS){
            locations.add(socialPostDTO.getLocation().getName());
            //System.out.println(socialPostDTO.getLocation().getName());
            numberOfLikes += likeReactionService.countLikesOnPost(socialPostDTO);
            numberOfComments += commentService.countCommentsOnPost(socialPostDTO);
        }
        int numberOfFollowers = Math.toIntExact(this.countAllFollowers(appUserDTO));
        int numberOfFollowing = Math.toIntExact(this.countAllFollowing(appUserDTO));
        int numberOfLocations = locations.size();
        int goodGrade = numberOfLikes+numberOfComments+numberOfFollowers+numberOfLocations;
        int badGrade = numberOfPosts+numberOfFollowing;

        double averageGrade = round(goodGrade*1.0/badGrade,2);
        System.out.println(goodGrade);
        System.out.println(badGrade);
        System.out.println(averageGrade);
        Map<String, Object> map = new HashMap<>();
        map.put("numberOfPosts",numberOfPosts);
        map.put("numberOfFollowers",numberOfFollowers);
        map.put("numberOfFollowing",numberOfFollowing);
        map.put("numberOfLocations",numberOfLocations);
        map.put("numberOfLikes",numberOfLikes);
        map.put("numberOfComments",numberOfComments);
        map.put("averageGrade",averageGrade);
        return map;
    }
    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
    public UserDetails findUserDetailsByEmail(String email){
        AppUserDTO user=  appUserMapper.toDto(appUserRepository.findAppUserByEmail(email));
        AppUser u = appUserMapper.toEntity(user);
        String password = user.getPassword();

        return new org.springframework.security.core.userdetails.User(
                email,
                password,
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
    }
    public AppUserDTO findByEmail(String email){
        System.out.println(email);
        System.out.println(appUserMapper.toDto(appUserRepository.findAppUserByEmail(email)));
        return appUserMapper.toDto(appUserRepository.findAppUserByEmail(email));

    }
    public Map<String,Object> registerUser(AppUserDTO user){
        String encodedPass = passwordEncoder.bCryptPasswordEncoder().encode(user.getPassword());
        user.setPassword(encodedPass);
        AppUser u = appUserRepository.save(appUserMapper.toEntity(user));
        user = appUserMapper.toDto(u) ;
        String token = jwtUtil.generateToken(user.getEmail());
        return Collections.singletonMap("token", token);
    }

    public AppUserDTO updateUser(AppUserDTO userToBeUpdated){
        AppUserDTO newAppUser = appUserMapper.toDto(appUserRepository.findById(userToBeUpdated.getId()).get());
        //userToBeUpdated.setPassword(newAppUser.getPassword());
        newAppUser.setFirstName(userToBeUpdated.getFirstName());
        newAppUser.setLastName(userToBeUpdated.getLastName());
        newAppUser.setEmail(userToBeUpdated.getEmail());
        return appUserMapper.toDto(appUserRepository.save(appUserMapper.toEntity(newAppUser)));
    }

    public AppUserDTO updateUserPassword(AppUserDTO userToBeUpdated){
        AppUserDTO newAppUser = appUserMapper.toDto(appUserRepository.findById(userToBeUpdated.getId()).get());
        String encodedPass = passwordEncoder.bCryptPasswordEncoder().encode(userToBeUpdated.getPassword());
        newAppUser.setPassword(encodedPass);
        return appUserMapper.toDto(appUserRepository.save(appUserMapper.toEntity(newAppUser)));
    }

    public void deleteUser(Long userId){
        boolean exists = appUserRepository.existsById(userId);
        if(!exists){
            throw new IllegalStateException("User with id "+userId+" does not exist");
        }
        appUserRepository.deleteById(userId);
    }

    public FollowDTO follow(Long userId, Long followedUserId){
        AppUserDTO user = appUserRepository.findById(userId).map(appUserMapper::toDto).get();
        AppUserDTO followedUser = appUserRepository.findById(followedUserId).map(appUserMapper::toDto).get();

        FollowDTO followDTO = new FollowDTO();
        followDTO.setTo(followedUser);
        followDTO.setFrom(user);

        return followService.addFollow(followDTO);
    }

    public boolean unfollow(Long from,Long to){
        AppUserDTO fromUser = appUserRepository.findById(from).map(appUserMapper::toDto).get();
        AppUserDTO toUser = appUserRepository.findById(to).map(appUserMapper::toDto).get();
        return followService.deleteFollow(fromUser, toUser);
    }

    public Long countAllFollowing(AppUserDTO appUserDTO){
        return followService.countAllFollowing(appUserDTO);
    }

    public Long countAllFollowers(AppUserDTO appUserDTO){
        return followService.countAllFollowers(appUserDTO);
    }

    public List<FollowDTO> getAllFollows(){
        return followService.getAllFollows();
    }


}

