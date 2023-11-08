package com.look4.demo.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "like_reaction")
public class LikeReaction implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    @ManyToOne
    @JsonIgnoreProperties(value = { "likeReactions" }, allowSetters = true)
    //@org.springframework.data.annotation.Transient
    private AppUser appUser;

    /*
    @OneToOne(mappedBy = "likeReaction")
    @JsonIgnoreProperties(value = { "likeReaction" }, allowSetters = true)
    private AppUser appUser;*/

    @ManyToOne
    @JsonIgnoreProperties(value = { "likeReactions" }, allowSetters = true)
    //@org.springframework.data.annotation.Transient
    private SocialPost socialPost;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AppUser getAppUser() {
        return appUser;
    }

    public void setAppUser(AppUser appUser) {
        this.appUser = appUser;
    }

    public SocialPost getSocialPost() {
        return socialPost;
    }

    public void setSocialPost(SocialPost socialPost) {
        this.socialPost = socialPost;
    }
}
