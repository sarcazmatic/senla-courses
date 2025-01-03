package com.senla.courses.util;

import com.senla.courses.model.Privilege;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserDetailsService userDetailsService;

    @Autowired
    public SecurityConfig(@Qualifier("customUserDetailsService") UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeHttpRequests(
                        (requests) -> requests
                                .requestMatchers("/", "/special/**", "/all/**").permitAll()
                                .requestMatchers("/student/**").hasAuthority(Privilege.STUDENT.getPrivilege())
                                .requestMatchers("/teacher/**").hasAuthority(Privilege.TEACHER.getPrivilege())
                                .requestMatchers("/admin/**").hasAuthority(Privilege.ADMIN.getPrivilege())
                                .anyRequest().authenticated()
                )
                .formLogin(
                        AbstractAuthenticationFilterConfigurer::permitAll
                )
                .httpBasic(withDefaults());
        return http.build();
    }

    /*@Bean
    public UserDetailsService userDetailsService() {
        UserDetails student =
                User.withDefaultPasswordEncoder()
                        .username("student")
                        .password("student")
                        .authorities(Role.STUDENT.getAuthorities())
                        .build();
        UserDetails teacher =
                User.withDefaultPasswordEncoder()
                        .username("teacher")
                        .password("teacher")
                        .authorities(Role.TEACHER.getAuthorities())
                        .build();
        UserDetails admin =
                User.withDefaultPasswordEncoder()
                        .username("admin")
                        .password("admin")
                        .authorities(Role.ADMIN.getAuthorities())
                        .build();
        return new InMemoryUserDetailsManager(student, teacher, admin);
    }*/

    @Bean
    protected DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        return daoAuthenticationProvider;
    }

}
