package com.look4.demo.dto;

import com.look4.demo.entities.AppUser;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.HashSet;
import java.util.Set;

public class FollowDTO {

    Long Id;

    private AppUserDTO from;

    private AppUserDTO to;

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public AppUserDTO getFrom() {
        return from;
    }

    public void setFrom(AppUserDTO from) {
        this.from = from;
    }

    public AppUserDTO getTo() {
        return to;
    }

    public void setTo(AppUserDTO to) {
        this.to = to;
    }
}
