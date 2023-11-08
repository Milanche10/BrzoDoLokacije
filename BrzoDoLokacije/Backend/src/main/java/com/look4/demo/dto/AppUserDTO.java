package com.look4.demo.dto;

import com.look4.demo.entities.*;


import javax.persistence.OneToMany;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class AppUserDTO {


    Long id;

    private String email;

    private String firstName;

    private String lastName;

    private String password;

    private Set<LikeReactionSlimDTO> likeReactions = new HashSet<>();

    private Set<CommentSlimDTO> comments = new HashSet<>();

    private ImageDTO image;

    private Set<SocialPostDTO> socialPosts = new HashSet<>();

    private List<Follow> followers;

    private List<Follow> following;




    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ImageDTO getImage() {
        return image;
    }

    public void setImage(ImageDTO image) {
        this.image = image;
    }

    public Set<SocialPostDTO> getSocialPosts() {
        return socialPosts;
    }

    public void setSocialPosts(Set<SocialPostDTO> socialPosts) {
        this.socialPosts = socialPosts;
    }

    public Set<LikeReactionSlimDTO> getLikeReactions() {
        return likeReactions;
    }

    public void setLikeReactions(Set<LikeReactionSlimDTO> likeReactions) {
        this.likeReactions = likeReactions;
    }

    public Set<CommentSlimDTO> getComments() {
        return comments;
    }

    public void setComments(Set<CommentSlimDTO> comments) {
        this.comments = comments;
    }

    public List<Follow> getFollowers() {
        return followers;
    }

    public void setFollowers(List<Follow> followers) {
        this.followers = followers;
    }

    public List<Follow> getFollowing() {
        return following;
    }

    public void setFollowing(List<Follow> following) {
        this.following = following;
    }
}
