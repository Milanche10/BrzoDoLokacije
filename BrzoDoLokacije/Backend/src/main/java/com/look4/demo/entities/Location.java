package com.look4.demo.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Table(name = "location")
@Entity
public class Location implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private double lon;
    private double lat;
    private String name;

    @OneToMany(mappedBy = "location", orphanRemoval = true)
    @JsonIgnoreProperties(
            value = {},
            allowSetters = true
    )
    @Transient
    private Set<SocialPost> socialPosts = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<SocialPost> getSocialPosts() {
        return socialPosts;
    }

    public void setSocialPosts(Set<SocialPost> socialPosts) {
        this.socialPosts = socialPosts;
    }
}
