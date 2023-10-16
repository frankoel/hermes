package com.application.hermesteamsphere.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashSet;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception
	{

		http.csrf().requireCsrfProtectionMatcher(new RequiresCsrfMatcher());

		http.addFilterAfter(new JWTAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class)
			.authorizeRequests()
				.antMatchers("/company*").authenticated()
				.antMatchers("/company/**").authenticated()
				.antMatchers("/dedication/*").authenticated()
				.antMatchers("/dedication/**").authenticated()
				.antMatchers("/project/*").authenticated()
				.antMatchers("/project/**").authenticated()
				.antMatchers(HttpMethod.POST,"/user").authenticated()
				.antMatchers(HttpMethod.PUT,"/user").authenticated()
				.antMatchers(HttpMethod.DELETE,"/user").authenticated()
				.antMatchers(HttpMethod.POST, "/user/login").permitAll()
	        	.anyRequest().authenticated();
    }

	@Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/v2/api-docs");
    }

	private static final class RequiresCsrfMatcher implements RequestMatcher {
		private final HashSet<String> allowedMethods;

		private RequiresCsrfMatcher() {
			this.allowedMethods = new HashSet(Arrays.asList("GET", "HEAD", "TRACE", "OPTIONS", "POST", "PUT", "DELETE"));
		}

		public boolean matches(HttpServletRequest request) {
			return !this.allowedMethods.contains(request.getMethod());
		}
	}
}
