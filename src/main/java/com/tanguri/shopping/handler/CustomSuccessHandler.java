package com.tanguri.shopping.handler;

import com.tanguri.shopping.domain.dto.oauth2.CustomOAuth2User;
import com.tanguri.shopping.domain.dto.user.CustomUserDetails;
import com.tanguri.shopping.jwt.JWTUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

@Component
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JWTUtil jwtUtil;

    public CustomSuccessHandler(JWTUtil jwtUtil) {

        this.jwtUtil = jwtUtil;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Authentication authentication) throws IOException, ServletException {
        //OAuth2User
        Object principal = authentication.getPrincipal();
        if (principal instanceof CustomUserDetails) {
            CustomUserDetails customUserDetails = (CustomUserDetails) principal;
            // JWT 생성 및 쿠키에 저장하는 로직 등 일반 로그인 관련 처리
            String token = jwtUtil.createJwt(customUserDetails.getUsername(),customUserDetails.getUserEntity().getRole(),60*60*60L);
            response.addCookie(createCookie("Authorization", token));
        } else if (principal instanceof CustomOAuth2User) {
            CustomOAuth2User customOAuth2User = (CustomOAuth2User) principal;
            // JWT 생성 및 쿠키에 저장하는 로직 등 OAuth2 로그인 관련 처리
            String role = getRole(authentication);
            String token = jwtUtil.createJwt(customOAuth2User.getUsername(),role,60*60*60L);
            response.addCookie(createCookie("Authorization", token));
        } else {
            throw new IllegalStateException("Unexpected authentication principal type: " + principal.getClass());
        }
//        CustomOAuth2User customUserDetails = (CustomOAuth2User) authentication.getPrincipal();
//
//        String username = customUserDetails.getUsername();
//
//        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
//        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
//        GrantedAuthority auth = iterator.next();
//        String role = auth.getAuthority();
//
//        String token = jwtUtil.createJwt(username, role, 60*60*60L);
//
//        response.addCookie(createCookie("Authorization", token));
//        response.sendRedirect("/");

        String prevPage = (String) request.getSession().getAttribute("prevPage");
        response.sendRedirect(prevPage);
    }

    private static String getRole(@NotNull Authentication authentication) {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();
        return role;
    }

    private Cookie createCookie(String key, String value) {

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(60*60*60);
        //cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setHttpOnly(true);

        return cookie;
    }
}
