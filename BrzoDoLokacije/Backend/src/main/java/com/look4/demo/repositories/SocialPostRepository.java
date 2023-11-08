package com.look4.demo.repositories;

import com.look4.demo.dto.AppUserDTO;
import com.look4.demo.dto.SocialPostDTO;
import com.look4.demo.entities.AppUser;
import com.look4.demo.entities.Location;
import com.look4.demo.entities.SocialPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SocialPostRepository extends JpaRepository<SocialPost,Long> {
    List<SocialPost> findSocialPostByAppUser(AppUser appUser);
    List<SocialPost> getSocialPostsByLocation(Location location);
    Long countSocialPostsByAppUser(AppUser appUser);
    SocialPost getSocialPostById(Long socialPostId);

}
