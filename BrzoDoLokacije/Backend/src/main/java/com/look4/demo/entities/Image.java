package com.look4.demo.entities;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.persistence.*;
import java.io.Serializable;

@Table(name = "image")
@Entity
public class Image implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String url;
    @OneToOne(mappedBy = "image")
    @JsonIgnoreProperties(value = { "image" }, allowSetters = true)
    private AppUser appUser;

    @ManyToOne
    @JsonIgnoreProperties(value = { "images" }, allowSetters = true)
    @org.springframework.data.annotation.Transient
    private SocialPost socialPost;

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
