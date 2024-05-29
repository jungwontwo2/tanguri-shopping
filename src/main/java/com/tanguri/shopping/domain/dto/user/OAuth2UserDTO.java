package com.tanguri.shopping.domain.dto.user;

import com.tanguri.shopping.domain.entity.Cart;
import com.tanguri.shopping.domain.entity.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OAuth2UserDTO {
    private String role;
    private String name;
    private String username;
    private String email;
    private String address;
    private String detailAddress;
    private String addressNumber;
    private String phone;
    private Boolean isSeller;
    public static User SingUpDtoToEntity(Cart cart, String username, String email, String name,
                                         String addressNumber, String address, String detailAddress,
                                         String phone){
        return new User(cart,username,email, name, addressNumber,address, detailAddress, phone);
    }
}
