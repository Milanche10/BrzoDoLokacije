package com.look4.demo.dto;

public class LikeReactionDTO {
    private Long id;
    private AppUserDTO appUser;
    private SocialPostDTO socialPost;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AppUserDTO getAppUser() {
        return appUser;
    }

    public void setAppUser(AppUserDTO appUser) {
        this.appUser = appUser;
    }

    public SocialPostDTO getSocialPost() {
        return socialPost;
    }

    public void setSocialPost(SocialPostDTO socialPost) {
        this.socialPost = socialPost;
    }
}
