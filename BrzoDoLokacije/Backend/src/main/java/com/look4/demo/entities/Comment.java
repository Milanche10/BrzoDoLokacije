package com.look4.demo.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;

@Table(name = "comment")
@Entity
public class Comment implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String content;

    @ManyToOne
    @JsonIgnoreProperties(value = { "comments" }, allowSetters = true)
    //@org.springframework.data.annotation.Transient
    private SocialPost socialPost;

    @ManyToOne
    @JsonIgnoreProperties(value = { "comments" }, allowSetters = true)
    //@org.springframework.data.annotation.Transient
    private AppUser appUser;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String conent) {
        this.content = conent;
    }

    public SocialPost getSocialPost() {
        return socialPost;
    }

    public void setSocialPost(SocialPost socialPost) {
        this.socialPost = socialPost;
    }

    public AppUser getAppUser() {
        return appUser;
    }

    public void setAppUser(AppUser appUser) {
        this.appUser = appUser;
    }
}
