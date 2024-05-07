//package com.tanguri.shopping.config;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.annotation.Order;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.AuthenticationFailureHandler;
//import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
//
//@Configuration
//@EnableWebSecurity
//@RequiredArgsConstructor
//@Order(1)
//public class SellerSecurityConfig {
//
//    private final AuthenticationFailureHandler CustomAuthFailureHandler;
//
//    @Bean//시큐리티 필터
//    public SecurityFilterChain SellerFilterChain(HttpSecurity http) throws Exception {
//        http.authorizeHttpRequests((auth) -> auth
//                .requestMatchers("/admin").hasRole("ADMIN")//admin페이지에는 ADMIN이라는 Role을 가지고 있어야 가능
//                .requestMatchers("/users/my/**", "/contents/write", "/contents/comment/**", "/contents/editPage/**", "/contents/delete/**").hasAnyRole("ADMIN", "USER")//여기는 ADMIN이나 USER 둘중 아무거나 있으면 가능
//                .requestMatchers("/", "/user/login", "/user/signup/**","/user/signup","/join/loginIdCheck","/join/nickNameCheck","/contents/**","/contents","/error").permitAll()//해당 사이트면 모두 허용
//                .anyRequest().authenticated()//나머지는 로그인 했으면 가능
//        );
//
//        http.formLogin((auth) -> auth.loginPage("/seller/login")//로그인 페이지
//                .loginProcessingUrl("/seller/login")//포스트 보내면 어디로 가는지
//                .usernameParameter("username")
//                .failureHandler(CustomAuthFailureHandler)
//                .defaultSuccessUrl("/seller/home")
//                .permitAll());
//
//
//        http.logout((logout) -> logout.logoutRequestMatcher(new AntPathRequestMatcher("/user/logout"))
//                .logoutSuccessUrl("/")
//                .invalidateHttpSession(true));
//        return http.build();
//    }
//
//}
