package com.company.bus_mgmt.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtTokenProvider tokens;
    private final AuthEntryPoint entryPoint;

    public SecurityConfig(JwtTokenProvider tokens, AuthEntryPoint entryPoint) {
        this.tokens = tokens;
        this.entryPoint = entryPoint;
    }

    @Bean
    public SecurityFilterChain chain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .exceptionHandling(h -> h.authenticationEntryPoint(entryPoint))
                .sessionManagement(s -> s.sessionCreationPolicy(org.springframework.security.config.http.SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(reg -> reg
                        .requestMatchers("/v3/api-docs/**","/swagger-ui/**","/swagger-ui.html","/actuator/health"
                                ,"/v3/api-docs.yaml","/v3/api-docs/swagger-config").permitAll()
                        .requestMatchers(org.springframework.http.HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/auth/login", "/api/auth/refresh","/api/auth/public/register").permitAll()
//                        .requestMatchers(HttpMethod.POST, "/api/auth/public/register", "/api/public/register").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/bookings/search", "/api/trips/*/seats","/api/routes", "/api/routes/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/routes/**").hasAnyRole("OPS_MANAGER","ADMIN")
                        .requestMatchers(HttpMethod.PUT,  "/api/routes/**").hasAnyRole("OPS_MANAGER","ADMIN")
                        .requestMatchers(HttpMethod.DELETE,"/api/routes/**").hasAnyRole("OPS_MANAGER","ADMIN")

                        .requestMatchers(HttpMethod.POST, "/api/trips").hasAnyRole("OPS_MANAGER","ADMIN","IT_TECH")
                        .requestMatchers(HttpMethod.PUT,  "/api/trips/**").hasAnyRole("OPS_MANAGER","ADMIN","IT_TECH")
                        .requestMatchers(HttpMethod.DELETE,"/api/trips/**").hasAnyRole("OPS_MANAGER","ADMIN","IT_TECH")
                        .requestMatchers(HttpMethod.GET,  "/api/trips/**").hasAnyRole("OPS_MANAGER","ADMIN","IT_TECH")
                        .requestMatchers(HttpMethod.POST, "/api/trips/*:deactivate", "/api/trips/*:activate", "/api/trips/*:complete-if-past")
                        .hasAnyRole("OPS_MANAGER","ADMIN","IT_TECH")

                        .requestMatchers(HttpMethod.GET,  "/api/trips").hasAnyRole("OPS_MANAGER","ADMIN","IT_TECH")
                        .requestMatchers(HttpMethod.GET,  "/api/trips/compact").hasAnyRole("OPS_MANAGER","ADMIN","IT_TECH")

                        .requestMatchers(HttpMethod.POST, "/api/assignments/by-lookup").hasAnyRole("OPS_MANAGER","ADMIN","IT_TECH")
                        .requestMatchers(HttpMethod.GET,  "/api/assignments/available-buses", "/api/assignments/available-drivers")
                        .hasAnyRole("OPS_MANAGER","ADMIN","IT_TECH")

                        .requestMatchers(HttpMethod.POST, "/api/stops").hasAnyRole("OPS_MANAGER","ADMIN","IT_TECH")
                        .requestMatchers(HttpMethod.PUT,   "/api/stops/**").hasAnyRole("OPS_MANAGER","ADMIN","IT_TECH")
                        .requestMatchers(HttpMethod.DELETE,"/api/stops/**").hasAnyRole("OPS_MANAGER","ADMIN","IT_TECH")
                        .requestMatchers(HttpMethod.GET,   "/api/stops").hasAnyRole("OPS_MANAGER","ADMIN","IT_TECH")

                                .anyRequest().authenticated()

                )

                .addFilterBefore(new JwtAuthenticationFilter(tokens), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
