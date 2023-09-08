package com.application.hermesteamsphere.security;

import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception{

        http.authorizeRequests()
                .antMatchers(HttpMethod.GET,"/company*").permitAll()
                .antMatchers(HttpMethod.POST, "/company*").permitAll();

        http.csrf().disable()
                    .authorizeRequests()
                    .antMatchers("/accessDenied*").permitAll()
                    .antMatchers("/closeTab*").permitAll()
                    .antMatchers(HttpMethod.GET, "/company*").permitAll()
                    .antMatchers(HttpMethod.POST, "/company*").permitAll()
                    .antMatchers(HttpMethod.POST, "/mainView*").authenticated()
                    .anyRequest().authenticated()
                .and()
                    .exceptionHandling().accessDeniedPage("/accessDenied.xhtml")
                .and()
                .logout()
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                    .logoutSuccessUrl("/closeTab.xhtml")
                    .invalidateHttpSession(true)
                    .deleteCookies("JSESSIONID")
                .and()
                .sessionManagement()
                    .invalidSessionUrl("/expired")
                    .maximumSessions(1)
                    .expiredUrl("/expired");

    }
}
