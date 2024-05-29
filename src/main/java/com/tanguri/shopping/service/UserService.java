package com.tanguri.shopping.service;

import com.tanguri.shopping.domain.dto.error.CustomException;
import com.tanguri.shopping.domain.dto.error.ErrorCode;
import com.tanguri.shopping.domain.dto.user.OAuth2UserDTO;
import com.tanguri.shopping.domain.dto.user.UserModifyDto;
import com.tanguri.shopping.domain.dto.user.UserSignUpDto;
import com.tanguri.shopping.domain.entity.*;
import com.tanguri.shopping.repository.CartRepository;
import com.tanguri.shopping.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final CartRepository cartRepository;

    @Transactional
    public void saveUser(UserSignUpDto userSignUpDto){
        Cart cart = new Cart();
        User user = UserSignUpDto.SingUpDtoToEntity(cart,userSignUpDto.getUsername(),bCryptPasswordEncoder.encode(userSignUpDto.getPassword())
                ,userSignUpDto.getEmail(),userSignUpDto.getName()
                ,userSignUpDto.getAddressNumber(),userSignUpDto.getAddress()
                ,userSignUpDto.getDetailAddress(),userSignUpDto.getIsSeller(),userSignUpDto.getPhone());
        cart.setUser(user);
        cartRepository.save(cart);
        userRepository.save(user);
    }
    @Transactional
    public void saveUser(OAuth2UserDTO userSignUpDto){
        Cart cart = new Cart();
        User user = OAuth2UserDTO.SingUpDtoToEntity(cart,userSignUpDto.getUsername()
                ,userSignUpDto.getEmail(),userSignUpDto.getName()
                ,userSignUpDto.getAddressNumber(),userSignUpDto.getAddress()
                ,userSignUpDto.getDetailAddress(),userSignUpDto.getPhone());
        cart.setUser(user);
        cartRepository.save(cart);
        userRepository.save(user);
    }


    public User getUserByLoginId(String loginId) {
        return userRepository.findByLoginId(loginId).orElse(null);
    }

    public Cart getCartByLoginId(Long id){
        return cartRepository.findById(id).orElse(null);
    }
    public Cart getCartByUserId(Long id){
        return cartRepository.findCartByUserId(id);
    }

    public void chargeMoney(Long id, int amount) {
        User user = userRepository.findById(id).orElse(null);
        user.addMoney(amount);
        userRepository.save(user);
    }

    public User findUser(Long id) {
        return userRepository.findByUserId(id).get();
    }
    public User findUserByUsername(String username){
        return userRepository.findByUsername(username);
    }

    public Integer getMoney(Long id){
        return userRepository.findById(id).get().getMoney();
    }

    public void modifyUserInfo(Long id, UserModifyDto userModifyDto) {
        User user = userRepository.findByUserId(id).orElse(null);
        user.modifyUserInfo(userModifyDto);
        userRepository.save(user);
    }

    public void updateRole(Long id, String role) {
        User user = userRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        user.updateRole(role);
        userRepository.save(user);
    }
}
