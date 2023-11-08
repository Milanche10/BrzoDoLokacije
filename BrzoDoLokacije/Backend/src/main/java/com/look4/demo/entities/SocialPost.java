package com.look4.demo.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Table(name = "social_post")
@Entity
public class SocialPost implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String description;
    private Instant date;
    @ManyToOne
    @JsonIgnoreProperties(
            value = {},
            allowSetters = true
    )
    private AppUser appUser;

    @OneToMany(mappedBy = "socialPost")
    @JsonIgnoreProperties(value = { "socialPost" }, allowSetters = true)
    private Set<Image> images = new HashSet<>();

    @OneToMany(mappedBy = "socialPost")
    @JsonIgnoreProperties(value = { "socialPost" }, allowSetters = true)
    private Set<LikeReaction> likeReactions = new HashSet<>();


    @OneToMany(mappedBy = "socialPost")
    @JsonIgnoreProperties(value = { "socialPost" }, allowSetters = true)
    private Set<Comment> comments = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(
            value = {},
            allowSetters = true
    )
    private Location location;

    public Long getId() {
        return id;
    }

    public AppUser getAppUser() {
        return appUser;
    }

    public void setAppUser(AppUser appUser) {
        this.appUser = appUser;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getDate() {
        return date;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Set<Image> getImages() {
        return images;
    }

    public void setImages(Set<Image> images) {
        this.images = images;
    }

    public Set<LikeReaction> getLikeReactions() {
        return likeReactions;
    }

    public void setLikeReactions(Set<LikeReaction> likeReactions) {
        this.likeReactions = likeReactions;
    }

    public Set<Comment> getComments() {
        return comments;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }
}
