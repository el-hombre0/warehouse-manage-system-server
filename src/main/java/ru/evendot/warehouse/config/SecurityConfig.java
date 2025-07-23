//package ru.evendot.warehouse.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig {
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
//        httpSecurity.
//                authorizeHttpRequests(request -> request
//                .requestMatchers("/api/v1/product-service/get-products",
//                        "/api/v1/product-service/product").permitAll()
//                        .anyRequest().authenticated());
//        return httpSecurity.build();
//
//    }
//}
