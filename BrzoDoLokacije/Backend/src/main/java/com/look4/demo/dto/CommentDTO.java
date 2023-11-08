package com.look4.demo.dto;

public class CommentDTO {
    private Long id;
    private String content;
    private AppUserDTO appUser;
    private SocialPostDTO socialPost;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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
