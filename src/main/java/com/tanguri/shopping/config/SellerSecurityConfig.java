//package com.tanguri.shopping.config;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.annotation.Order;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.AuthenticationFailureHandler;
//
//@Configuration
//@EnableWebSecurity
//@RequiredArgsConstructor
//@Order(2)
//public class SellerSecurityConfig {
//
//    private final AuthenticationFailureHandler CustomAuthFailureHandler;
//
//    @Bean//시큐리티 필터
//    public SecurityFilterChain SellerFilterChain(HttpSecurity http) throws Exception {
//        http.authorizeHttpRequests((auth) -> auth
//                .requestMatchers("/seller/login").authenticated()
//                .anyRequest().permitAll()
//        );
//
//        http.csrf((auth)->auth.disable());
////        http.authorizeHttpRequests((auth) -> auth
//////                .requestMatchers("/seller/my/**").hasAnyRole("ADMIN", "SELLER")//여기는 ADMIN이나 USER 둘중 아무거나 있으면 가능
////                .requestMatchers("/user/login","/").permitAll()
////                .anyRequest().permitAll()
////        );
////
////        http.formLogin((auth) -> auth.loginPage("/seller/login")//로그인 페이지
////                .loginProcessingUrl("/seller/login")//포스트 보내면 어디로 가는지
////                .usernameParameter("username")
////                .failureHandler(CustomAuthFailureHandler)
////                .defaultSuccessUrl("/")
////                .permitAll()
////        );
////
////
//////        http.logout((logout)->logout.logoutRequestMatcher(new AntPathRequestMatcher("/seller/logout"))
//////                .logoutSuccessUrl("/")
//////                .invalidateHttpSession(true));
////
////
////        http.csrf((auth)->auth.disable());
////
////        //http.sessionManagement((auth) -> auth.maximumSessions(1).maxSessionsPreventsLogin(true));//true: 새로운 로그인 차단 false: 기존 세션 하나 삭제
////
////        //http.sessionManagement((session) -> session.sessionFixation((sessionFixation) -> sessionFixation.newSession()));//로그인 시 동일한 세션에 대한 id 변경
////
////
//        return http.build();
//    }
//}
