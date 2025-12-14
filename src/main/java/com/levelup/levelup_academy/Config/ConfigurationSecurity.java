package com.levelup.levelup_academy.Config;

import java.util.List;
import lombok.RequiredArgsConstructor;

import com.levelup.levelup_academy.Service.MyUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class ConfigurationSecurity {

    private final MyUserDetailsService myUserDetailsService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter; // فلتر JWT الجديد

    // === CORS (Security-managed) ===
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration cfg = new CorsConfiguration();
        cfg.setAllowedOrigins(List.of(
                "http://localhost:8081",
                "http://localhost:8080",
                "http://localhost:5173",
                "https://288dba31-1857-4603-ad60-81a461a957dd.lovableproject.com"
        ));
        cfg.setAllowedMethods(List.of("GET","POST","PUT","PATCH","DELETE","OPTIONS"));
        cfg.setAllowedHeaders(List.of("*"));
        cfg.setExposedHeaders(List.of("Authorization"));
        cfg.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", cfg);
        return source;
    }

    // === Password Encoder ===
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // === Auth Provider ===
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(myUserDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    // نحتاج AuthenticationManager عشان تستخدمه في login endpoint لتوليد الـ JWT
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    // === Security Filter Chain (JWT / stateless) ===
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .cors(cors -> {})                                // CORS من الـ Bean فوق
                .csrf(AbstractHttpConfigurer::disable)           // لا نحتاج CSRF مع JWT
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // مهم
                .authenticationProvider(daoAuthenticationProvider())
                .authorizeHttpRequests(auth -> auth
                        // ======== Public Static Resources (HTML, CSS, JS) ========
                        .requestMatchers(
                                "/",
                                "/home.html",
                                "/index.html",
                                "/login.html",
                                "/signup.html",
                                "/register-*.html",
                                "/role-selector.html",
                                "/pending-approval.html",
                                "/config.js",
                                "/*.html",
                                "/css/**",
                                "/js/**",
                                "/images/**"
                        ).permitAll()
                        
                        // ======== Public endpoints (بدون JWT) ========
                        .requestMatchers(
                                "/api/v1/parent/register",
                                "/api/v1/player/register",
                                "/api/v1/trainer/register",
                                "/api/v1/pro/register",
                                "/api/v1/contract/**",
                                "/api/v1/payments/**",
                                "/api/v1/auth/login"      // ضع هنا login اللي يرجّع JWT
                        ).permitAll()

                        // ======== ROLE: PARENTS ========
                        .requestMatchers(
                                "/api/v1/parent/edit","/api/v1/parent/delete", "/api/v1/parent/add-child",
                                "/api/v1/parent/update-child", "/api/v1/parent/delete-child",
                                "/api/v1/parent/child-statistic","/api/v1/parent/get-games",
                                "/api/v1/parent/get-child-stati-by-parent"
                        ).hasAuthority("PARENTS")

                        // ======== SUBSCRIPTIONS (PARENTS, PLAYER) ========
                        .requestMatchers(
                                "/api/v1/subscription/basic","/api/v1/subscription/standard",
                                "/api/v1/subscription/premium"
                        ).hasAnyAuthority("PARENTS","PLAYER")

                        // ======== BOOKINGS (PLAYER, PARENTS, PRO) ========
                        .requestMatchers(
                                "/api/v1/booking/add", "/api/v1/booking/cancel", "/api/v1/booking/check",
                                "/api/v1/booking/get-all", "/api/v1/review/add","/api/v1/review/delete"
                        ).hasAnyAuthority("PLAYER","PARENTS","PRO")

                        // ======== MODERATOR & ADMIN (shared access) ========
                        .requestMatchers(
                                "/api/v1/parent/get", "/api/v1/player/get", "/api/v1/pro/get",
                                "/api/v1/trainer/get", "/api/v1/pro/cv/**", "/api/v1/trainer/cv/**"
                        ).hasAnyAuthority("MODERATOR", "ADMIN")

                        // ======== MODERATOR only ========
                        .requestMatchers(
                                "/api/v1/game/**","/api/v1/contract/**", "/api/v1/moderator/edit",
                                "/api/v1/moderator/delete", "/api/v1/moderator/get-all-pro",
                                "/api/v1/moderator/review-contract","/api/v1/moderator/send-exam",
                                "/api/v1/player/get-player", 
                                "/api/v1/review/get-all", "/api/v1/session/get",
                                "/api/v1/session/add","/api/v1/session/update","/api/v1/session/del",
                                "/api/v1/session/change-session", "/api/v1/moderator/promote"
                        ).hasAuthority("MODERATOR")

                        // ======== PLAYER ========
                        .requestMatchers(
                                "/api/v1/player/edit","/api/v1/player/delete","/api/v1/player/player"
                        ).hasAuthority("PLAYER")

                        // ======== PRO ========
                        .requestMatchers(
                                "/api/v1/pro/edit","/api/v1/pro/delete","/api/v1/pro/accept","/api/v1/pro/reject",
                                "/api/v1/pro/expireAccount","/api/v1/pro/professional"
                        ).hasAuthority("PRO")

                        // ======== ADMIN only ========
                        .requestMatchers(
                                "/api/v1/pro/approve/**","/api/v1/pro/reject/**",
                                "/api/v1/user/get-all",
                                "/api/v1/trainer/approve-trainer/**","/api/v1/trainer/reject-trainer/**",
                                "/api/v1/moderator/register"
                        ).hasAuthority("ADMIN")

                        // ======== TRAINER ========
                        .requestMatchers(
                                "/api/v1/review/get-my-reviews","/api/v1/session/notify-start",
                                "/api/v1/trainer/get-players","/api/v1/child-statistic/**",
                                "/api/v1/player-statistic/**","/api/v1/pro-statistic/**",
                                "/api/v1/trainer/edit", "/api/v1/trainer/delete","/api/v1/trainer/give-player",
                                "/api/v1/trainer/give-pro","/api/v1/trainer/give-child",
                                "/api/v1/trainer/addStatisticToChild", "/api/v1/trainer/addStatisticToPlayer",
                                "/api/v1/trainer/addStatisticToPro", "/api/v1/trainer/send-promotion-request"
                        ).hasAuthority("TRAINER")

                        // أي شيء غير اللي فوق -> يحتاج JWT صحيح
                        .anyRequest().authenticated()
                )
                // ما نستخدم httpBasic مع JWT
                .httpBasic(AbstractHttpConfigurer::disable)
                // فلتر JWT لازم يشتغل قبل UsernamePasswordAuthenticationFilter
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        // logout هنا بس يمسح الـ SecurityContext (التوكن أنت تتحكم فيه من الفرونت)
        http.logout(logout -> logout
                .logoutUrl("/api/v1/user/logout")
        );

        return http.build();
    }
}
