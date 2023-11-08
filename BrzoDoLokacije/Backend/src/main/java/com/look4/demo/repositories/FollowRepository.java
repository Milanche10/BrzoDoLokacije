package com.look4.demo.repositories;

import com.look4.demo.dto.FollowDTO;
import com.look4.demo.entities.AppUser;
import com.look4.demo.entities.Follow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FollowRepository extends JpaRepository<Follow,Long> {
    boolean existsByFromAndTo(AppUser from, AppUser to);

    void deleteByFromAndTo(AppUser from, AppUser to);

    Follow findByFromAndTo(AppUser from,AppUser to);
    //Count Following grouped by from
    Long countAllByFrom(AppUser from);
    //Count Followers grouped by to
    Long countAllByTo(AppUser to);
}
