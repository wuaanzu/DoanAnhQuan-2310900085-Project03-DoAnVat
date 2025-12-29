package com.Daq.Mikifood.K23CNT1.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        // Admin routes - chỉ ADMIN mới truy cập được
                        .requestMatchers("/admin/**").hasRole("ADMIN")

                        // User routes - cần đăng nhập (ADMIN hoặc USER)
                        .requestMatchers("/orders/**", "/cart/**").hasAnyRole("ADMIN", "USER")

                        // Public routes - ai cũng truy cập được
                        .requestMatchers(
                                "/", "/home", "/products/**",
                                "/css/**", "/js/**", "/images/**", "/uploads/**",
                                "/webjars/**", "/login", "/register", "/error")
                        .permitAll()

                        // Tất cả request khác cần authentication
                        .anyRequest().authenticated())
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/home", true)
                        .failureUrl("/login?error=true")
                        .permitAll())
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout=true")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll())
                .exceptionHandling(ex -> ex
                        .accessDeniedPage("/access-denied"));

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
