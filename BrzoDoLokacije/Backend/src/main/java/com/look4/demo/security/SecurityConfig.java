package com.look4.demo.security;

import com.look4.demo.services.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.savedrequest.NullRequestCache;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.servlet.http.HttpServletResponse;


@Configuration
@EnableWebMvc
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        prePostEnabled = true
)
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    private JWTFilter filter;
    private MyUserDetailsService uds;

    @Autowired
    public SecurityConfig(JWTFilter filter,MyUserDetailsService uds){
        this.filter = filter;
        this.uds = uds;
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .httpBasic().disable()
                .cors()
                .and()
                .authorizeHttpRequests()
                .antMatchers("/api/auth/**").permitAll()
                .antMatchers("/api/user/**").hasRole("USER")
                .and()
                .userDetailsService(uds)
                .exceptionHandling()
                .authenticationEntryPoint(
                        (request, response, authException) ->
                                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized")
                )
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);

        /*
         http.csrf()
                .disable()
                .authorizeRequests()
                .anyRequest().fullyAuthenticated()
                .and().headers().frameOptions().sameOrigin()
                .and().requestCache().requestCache(new NullRequestCache())
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);*/
    }
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

}
