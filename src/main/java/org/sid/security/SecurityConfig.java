package org.sid.security;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;


@Configuration
@EnableWebSecurity
public class SecurityConfig  extends WebSecurityConfigurerAdapter{

	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
	
		auth.inMemoryAuthentication().withUser("admin").password("{noop}12345678").roles("ADMIN","USER");
		auth.inMemoryAuthentication().withUser("user").password("{noop}12345678").roles("USER");
		
	}
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.formLogin().loginPage("/login");
		http.authorizeRequests().antMatchers("/villes").hasRole("USER");
		http.authorizeRequests().antMatchers("/addCinema","/saveCinema","/editCinema","/deleteCinema").hasRole("ADMIN");
		http.exceptionHandling().accessDeniedPage("/403");
	}
	

	
}
