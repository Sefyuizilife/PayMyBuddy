package com.paymybuddy.paymybuddy.configurations;

import com.paymybuddy.paymybuddy.services.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * This class extends the {@link WebSecurityConfigurerAdapter} interface to customise the security rules automatically
 * defined by Spring Security with the {@link EnableWebSecurity} annotation.
 */
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final UserService           userService;
    private final BCryptPasswordEncoder encoder;

    /**
     * Create a new instance of {@link WebSecurityConfiguration}. This will be done automatically by SpringBoot with
     * dependency injection.
     *
     * @param userService
     *         instance of {@link UserService}.
     * @param encoder
     *         instance of {@link BCryptPasswordEncoder} that uses the BCrypt strong hashing function implementing
     *         {@link PasswordEncoder}.
     */
    public WebSecurityConfiguration(UserService userService, BCryptPasswordEncoder encoder) {

        this.userService = userService;
        this.encoder     = encoder;
    }

    /**
     * Any endpoint that requires defense against common vulnerabilities can be specified here, including public ones.
     *
     * @param http
     *         the {@link HttpSecurity} to modify
     *
     * @throws Exception
     *         if an error occurs.
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable();
        http.headers().frameOptions().disable();

        http.authorizeRequests().antMatchers("/static/**", "/h2-console/**").permitAll();

        http.authorizeRequests().anyRequest().authenticated();

        http.formLogin().loginPage("/login").permitAll().defaultSuccessUrl("/transactions");

        http.logout().logoutSuccessUrl("/login");
    }

    /**
     * Allows you to specify the {@link AuthenticationManager}.
     *
     * @param authManagerBuilder
     *         allows for easily building in memory authentication
     *
     * @throws Exception if an error occurs.
     */
    @Override
    protected void configure(AuthenticationManagerBuilder authManagerBuilder) throws Exception {

        authManagerBuilder.userDetailsService(userService).passwordEncoder(encoder);
    }

    /**
     * Expose le {@link AuthenticationManager} en tant que Bean.
     *
     * @return the {@link AuthenticationManager}.
     *
     * @throws Exception
     *         if an error occurs.
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {

        return super.authenticationManagerBean();
    }
}
