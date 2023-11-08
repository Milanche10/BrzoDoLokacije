package com.look4.demo.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
@Table(name = "app_user")
@Entity
@EnableJpaRepositories

public class AppUser implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    private String email;
    @Column(name = "first_name")
    private String firstName;
    @Column(name="last_name")
    private String lastName;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;


    @OneToOne
    @JoinColumn(unique = true)
    @JsonIgnoreProperties(value = { "appUser" }, allowSetters = true)
    private Image image;

    @OneToMany(mappedBy = "appUser", orphanRemoval = true)
    @JsonIgnoreProperties(value = { "appUser" }, allowSetters = true)
    @Transient
    private Set<LikeReaction> likeReactions = new HashSet<>();

    @OneToMany(mappedBy = "appUser", orphanRemoval = true)
    @JsonIgnoreProperties(value = { "appUser" }, allowSetters = true)
    @Transient
    private Set<Comment> comments = new HashSet<>();

    @OneToMany(mappedBy = "appUser", orphanRemoval = true)
    @JsonIgnoreProperties(value = { "appUser" }, allowSetters = true)
    @Transient
    private Set<SocialPost> socialPosts = new HashSet<>();

    @OneToMany(mappedBy="to")
    @JsonIgnore
    private List<Follow> followers;

    @OneToMany(mappedBy="from")
    @JsonIgnore
    private List<Follow> following;





    public Set<SocialPost> getSocialPosts() {
        return socialPosts;
    }

    public void setSocialPosts(Set<SocialPost> socialPosts) {
        this.socialPosts = socialPosts;
    }

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

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
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
