package com.tanguri.shopping.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity(debug = false)
@RequiredArgsConstructor
@Order(1)
public class UserSecurityConfig {

    private final AuthenticationFailureHandler CustomAuthFailureHandler;

    @Bean//비밀번호 암호화
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean//시큐리티 필터
    public SecurityFilterChain UserFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((auth) -> auth
                .requestMatchers("/admin").hasRole("ADMIN")//admin페이지에는 ADMIN이라는 Role을 가지고 있어야 가능
                .requestMatchers("/users/my/**","/contents/write","/contents/comment/**","/contents/editPage/**","/contents/delete/**").hasAnyRole("ADMIN", "USER")//여기는 ADMIN이나 USER 둘중 아무거나 있으면 가능
                .requestMatchers("/product/upload").hasRole("SELLER")
                .requestMatchers("/", "/user/login", "/logout","/user/signup/**","/user/signup","/seller/signup","/join/loginIdCheck","/join/nickNameCheck","/contents/**","/contents","/error").permitAll()//해당 사이트면 모두 허용
                .anyRequest().authenticated()//나머지는 로그인 했으면 가능

        );

        http.formLogin((auth) -> auth.loginPage("/user/login")//로그인 페이지
                .loginProcessingUrl("/user/login")//포스트 보내면 어디로 가는지
                .usernameParameter("username")
                .failureHandler(CustomAuthFailureHandler)
                .defaultSuccessUrl("/",false)
                .permitAll());


        http.logout((logout)->logout.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true));

        http.csrf((auth)->auth.disable());

        //http.sessionManagement((auth) -> auth.maximumSessions(1).maxSessionsPreventsLogin(true));//true: 새로운 로그인 차단 false: 기존 세션 하나 삭제

        //http.sessionManagement((session) -> session.sessionFixation((sessionFixation) -> sessionFixation.newSession()));//로그인 시 동일한 세션에 대한 id 변경


        return http.build();
    }

    @Bean
    SessionRegistry sessionRegistry(){
        SessionRegistryImpl registry = new SessionRegistryImpl();
        return registry;
    }



    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations())
                .requestMatchers("/css/**", "style.css");
    }

}
