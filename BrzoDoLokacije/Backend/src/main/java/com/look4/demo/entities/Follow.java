package com.look4.demo.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Table(name = "follow")
@Entity
@EnableJpaRepositories
public class Follow implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;


    @ManyToOne
    @JoinColumn(name="from_user_fk")
    @JsonIgnore
    @JsonManagedReference
    private AppUser from;

    @ManyToOne
    @JoinColumn(name="to_user_fk")
    @JsonIgnore
    @JsonManagedReference
    private AppUser to;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AppUser getFrom() {
        return from;
    }

    public void setFrom(AppUser from) {
        this.from = from;
    }

    public AppUser getTo() {
        return to;
    }

    public void setTo(AppUser to) {
        this.to = to;
    }
}
