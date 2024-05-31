package com.tanguri.shopping.config;

import com.tanguri.shopping.handler.CustomAuthenticationSuccessHandler;
import com.tanguri.shopping.handler.CustomSuccessHandler;
import com.tanguri.shopping.jwt.JWTFilter;
import com.tanguri.shopping.jwt.JWTUtil;
import com.tanguri.shopping.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity(debug = false)
@RequiredArgsConstructor
@Order(1)
public class UserSecurityConfig {

    private final AuthenticationFailureHandler CustomAuthFailureHandler;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomSuccessHandler customSuccessHandler;
    private final JWTUtil jwtUtil;

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
                .requestMatchers("/user/cart/**","/product/order/**").hasRole("BUYER")
                .requestMatchers("/", "/user/login", "/logout","/user/signup/**","/user/signup","/seller/signup","/join/loginIdCheck","/join/nickNameCheck","/contents/**","/contents","/error","product/**").permitAll()//해당 사이트면 모두 허용
                .anyRequest().authenticated()//나머지는 로그인 했으면 가능
//                        .anyRequest().permitAll()
        );

        http.formLogin((auth) -> auth.loginPage("/user/login")//로그인 페이지
                .loginProcessingUrl("/user/login")//포스트 보내면 어디로 가는지
                .usernameParameter("username")
                .successHandler(customSuccessHandler)
                .failureHandler(CustomAuthFailureHandler)
                .permitAll());

        http.oauth2Login((oauth2)->oauth2.userInfoEndpoint((userInfoEndpointConfig)->userInfoEndpointConfig
                        .userService(customOAuth2UserService))
                        .successHandler(customSuccessHandler));

        http.addFilterBefore(new JWTFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);


        http.logout((logout)->logout.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/")
                .deleteCookies("Authorization")
                .clearAuthentication(true)
                .invalidateHttpSession(true)
                .permitAll());

        http.csrf((auth)->auth.disable());

        http
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

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
                .requestMatchers("/css/**", "style.css","/files/**");
    }

}
