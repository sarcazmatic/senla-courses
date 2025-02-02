package com.senla.courses.config;

import com.senla.courses.model.Privilege;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final UserDetailsService customUserDetailsService;

    @Autowired
    public SecurityConfig(@Qualifier("customUserDetailsService") UserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf((csrf) -> csrf.disable())
                .authorizeHttpRequests(
                        (requests) -> requests
                                .requestMatchers("/", "/special/**", "/log", "/all/**", "/student/register", "/teacher/register").permitAll()
                                .requestMatchers("/student/**").hasAuthority(Privilege.STUDENT.getPrivilege())
                                .requestMatchers("/teacher/**").hasAuthority(Privilege.TEACHER.getPrivilege())
                                .requestMatchers("/admin/**").hasAuthority(Privilege.ADMIN.getPrivilege())
                                .anyRequest().authenticated()
                )
                .formLogin(login -> login.permitAll()
                )
                .httpBasic(withDefaults());
        return http.build();
    }

    @Bean
    protected DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(customUserDetailsService);
        return daoAuthenticationProvider;
    }

}
