package com.hvs.diploma.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final UserDetailsService userService;
    private final CustomLogoutHandler logoutHandler;

    @Autowired
    public SecurityConfig(OAuth2SuccessHandler oAuth2SuccessHandler, UserDetailsService userDetailsServiceImpl, CustomLogoutHandler logoutHandler) {
        this.oAuth2SuccessHandler = oAuth2SuccessHandler;
        this.userService = userDetailsServiceImpl;
        this.logoutHandler = logoutHandler;
    }

    @Bean
    public BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userService)
                .passwordEncoder(encoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/")
                .authenticated()
                .antMatchers("/static/**", "/templates/error/**")
                .permitAll()
                .antMatchers("/admin")
                .hasRole("ADMIN")
                .antMatchers("/support")
                .hasRole("COMMON_USER")
                .and()
                .oauth2Login()
                .loginPage("/login")
                .successHandler(oAuth2SuccessHandler)
                .permitAll()
                .and()
                .formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .usernameParameter("email")
                .passwordParameter("password")
                .permitAll()
                .and()
                .logout()
                .logoutUrl("/logout")
                .addLogoutHandler(logoutHandler)
                .logoutSuccessUrl("/login")
                .invalidateHttpSession(true)
                .permitAll()
                .and();
    }

}
