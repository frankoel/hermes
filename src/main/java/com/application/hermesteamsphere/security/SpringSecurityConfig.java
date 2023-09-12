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

	private static final String ACCESO_API = "ACCESO_API";

    @Override
    protected void configure(HttpSecurity http) throws Exception
	{

		http.csrf().requireCsrfProtectionMatcher(new RequiresCsrfMatcher());

		http.addFilterAfter(new JWTAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class)
			.authorizeRequests()
				.antMatchers("/company*").hasAnyRole(ACCESO_API)
				.antMatchers("/company/**").hasAnyRole(ACCESO_API)
				.antMatchers(HttpMethod.POST, "/user").permitAll()
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
