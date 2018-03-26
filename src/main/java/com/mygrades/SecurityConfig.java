package com.mygrades;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.mygrades.repositories.UtilisateurRepository;
import com.mygrades.services.UserDetailsServiceImpl;

@Configuration
@ComponentScan("com.mygrades")
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    
	@Autowired
	UtilisateurRepository urep;
	
	@Autowired
	UserDetailsServiceImpl userDetailsService;
	
	@Override
    protected void configure(HttpSecurity http) throws Exception {
		http
			.authorizeRequests()
				.antMatchers("/**").permitAll()
			.and()
				.csrf().disable();;

//		http
//			.authorizeRequests()
//				.antMatchers("/webjars/**").permitAll()
//				.antMatchers("/js/**").permitAll()
//				.antMatchers("/css/**").permitAll()
//				.antMatchers("/login").permitAll()
//				.anyRequest().authenticated()
//				.and()
//			.formLogin()
//				.loginPage("/login")
//				.and()
//			.logout()
//				.logoutUrl("/login?logout")
//				.and()
//			.csrf().disable();;

    }
	
	@Override
	protected void configure(AuthenticationManagerBuilder builder) throws Exception {
		builder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
	    return new BCryptPasswordEncoder();
	}
	
}