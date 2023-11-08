package com.look4.demo.dto;

import com.look4.demo.entities.SocialPost;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ImageDTO {
    private Long id;
    private String name;
    private String url;

    private SocialPostDTO socialPost;
    private AppUserDTO appUser;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public SocialPostDTO getSocialPost() {
        return socialPost;
    }

    public void setSocialPost(SocialPostDTO socialPost) {
        this.socialPost = socialPost;
    }

}
