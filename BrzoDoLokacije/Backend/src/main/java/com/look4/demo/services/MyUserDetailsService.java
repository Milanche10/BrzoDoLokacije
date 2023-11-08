package com.look4.demo.services;

import com.look4.demo.entities.AppUser;
import com.look4.demo.repositories.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
public class MyUserDetailsService implements UserDetailsService {
    @Autowired
    AppUserRepository appUserRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        AppUser userRes = appUserRepository.findAppUserByEmail(email);
        if(userRes==null)
            throw new UsernameNotFoundException("Could not findUser with email = " + email);
        AppUser user = userRes;
        return new org.springframework.security.core.userdetails.User(
                email,
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
    }
}
