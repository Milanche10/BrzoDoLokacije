package com.look4.demo.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class SocialPostDTO {
    private Long id;
    private String name;
    private String description;
    private Instant date;
    private LocationDTO location;
    private AppUserDTO appUser;

    private Set<LikeReactionSlimDTO> likeReactions = new HashSet<>();
    private Set<ImageSlimDTO> images = new HashSet<>();
    private Set<CommentSlimDTO> comments = new HashSet<>();

    public void setUser(AppUserDTO user) {
        this.appUser = user;
    }

    public LocationDTO getLocation() {
        return location;
    }

    public void setLocation(LocationDTO locationDTO) {
        this.location = locationDTO;
    }

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

    public Set<ImageSlimDTO> getImages() {
        return images;
    }

    public void setImages(Set<ImageSlimDTO> images) {
        this.images = images;
    }

    public AppUserDTO getAppUser() {
        return appUser;
    }

    public void setAppUser(AppUserDTO appUser) {
        this.appUser = appUser;
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
}
