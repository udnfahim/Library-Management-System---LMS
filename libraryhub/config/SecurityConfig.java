package com.libraryhub.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    UserDetailsService userDetailsService;


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, CustomAuthProvider customAuthProvider) throws Exception {

        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/assets/**", "/auth.css", "/*.jpg").permitAll()

                        .requestMatchers("/", "/registration", "/forgot-password").anonymous()

                        .requestMatchers("/dashboard", "/books-allotment", "/books-management",
                                "/student-management", "/vendor-management","/vendor-management/filter",
                                "/publications", "/subscriptions","/style.css",
                                "/profile","/reports","/purchase-books-filter","/allotment-history").authenticated()

                        .requestMatchers("/profile/delete","/profile/","/profile/update", "/profile/change-password",
                                "/vendor-management/delete","/subscriptions/delete",
                                "/student-management/delete","/publications/delete","/books-management/delete",
                                "/books-allotment-issue")
                        .fullyAuthenticated()

                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/")
                        .loginProcessingUrl("/")
                        .usernameParameter("username")
                        .passwordParameter("password")
                        .defaultSuccessUrl("/dashboard", false)
                        .failureUrl("/?error")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .permitAll()
                )
                .rememberMe(rm -> rm
                        .rememberMeParameter("remember")
                        .rememberMeCookieName("remember")
                        .tokenValiditySeconds(20 * 60)
                        .userDetailsService(userDetailsService)
                );

        return http.build();
    }

    @Bean
    public AuthenticationManager authManager(HttpSecurity http, CustomAuthProvider customAuthProvider) throws Exception {
        AuthenticationManagerBuilder authBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authBuilder.authenticationProvider(customAuthProvider);
        return authBuilder.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
