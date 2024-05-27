package com.tanguri.shopping.service;


import com.tanguri.shopping.domain.dto.oauth2.CustomOAuth2User;
import com.tanguri.shopping.domain.dto.oauth2.NaverResponse;
import com.tanguri.shopping.domain.dto.oauth2.OAuth2Response;
import com.tanguri.shopping.domain.dto.user.OAuth2UserDTO;
import com.tanguri.shopping.domain.entity.Cart;
import com.tanguri.shopping.domain.entity.User;
import com.tanguri.shopping.repository.CartRepository;
import com.tanguri.shopping.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final NaverPayAddressService naverPayAddressService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        String accessToken = userRequest.getAccessToken().getTokenValue();

        Map<String, String> payAddress = naverPayAddressService.getPayAddress(accessToken);
        System.out.println("oAuth2User = " + oAuth2User);
        System.out.println("payAddress = " + payAddress);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2Response oAuth2Response = null;
        if(registrationId.equals("naver")){
            oAuth2Response = new NaverResponse(oAuth2User.getAttributes());
        }
        else {
            return null;
        }
        String username = oAuth2Response.getProvider() + " " + oAuth2Response.getProviderId();
        User existData = userRepository.findByUsername(username);
        System.out.println(oAuth2Response.getName());
        if(existData==null){
            OAuth2UserDTO userDTO = new OAuth2UserDTO();
            userDTO.setUsername(username);
            userDTO.setName(oAuth2Response.getName());
            userDTO.setEmail(oAuth2Response.getEmail());
            userDTO.setAddressNumber(payAddress.get("zipCode"));
            userDTO.setAddress(payAddress.get("baseAddress"));
            userDTO.setDetailAddress(payAddress.get("detailAddress"));
            userDTO.setPhone(payAddress.get("telNo"));
            userDTO.setIsSeller(false);
            Cart cart = new Cart();
            cartRepository.save(cart);
            User user = OAuth2UserDTO.SingUpDtoToEntity(cart, username, oAuth2Response.getEmail(), oAuth2Response.getName(), payAddress.get("zipCode")
                    , payAddress.get("baseAddress"), payAddress.get("detailAddress"), false, payAddress.get("telNo"));
            userRepository.save(user);
            return new CustomOAuth2User(userDTO);
        }
        else {
            OAuth2UserDTO userDTO = new OAuth2UserDTO();
            userDTO.setUsername(username);
            userDTO.setName(oAuth2Response.getName());
            return new CustomOAuth2User(userDTO);
        }
    }
}
