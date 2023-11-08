package com.look4.demo.repositories;

import com.look4.demo.entities.AppUser;
import com.look4.demo.entities.Comment;
import com.look4.demo.entities.SocialPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment,Long> {
    Long countCommentBySocialPost(SocialPost socialPost);
    List<Comment> getCommentsBySocialPost(SocialPost socialPost);
    List<Comment> getCommentsByAppUser(AppUser appUser);
}
