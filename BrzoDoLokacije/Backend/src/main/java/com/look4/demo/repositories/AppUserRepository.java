package com.look4.demo.repositories;

import com.look4.demo.entities.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser,Long> {
    AppUser findAppUserByEmail(String email);
}
