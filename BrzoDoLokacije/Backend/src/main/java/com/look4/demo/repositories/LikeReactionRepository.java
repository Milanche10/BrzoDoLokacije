package com.look4.demo.repositories;

import com.look4.demo.entities.AppUser;
import com.look4.demo.entities.LikeReaction;
import com.look4.demo.entities.SocialPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LikeReactionRepository extends JpaRepository<LikeReaction,Long> {
    Long countLikeReactionBySocialPost(SocialPost socialPost);

    List<LikeReaction> getLikeReactionsBySocialPost(SocialPost socialPost);
    List<LikeReaction> getLikeReactionsByAppUser(AppUser appUser);
}
