package com.tanguri.shopping.service;

import com.tanguri.shopping.domain.dto.user.UserSignUpDto;
import com.tanguri.shopping.domain.entity.User;
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

    @Transactional
    public void saveUser(UserSignUpDto userSignUpDto){
        User user = UserSignUpDto.SingUpDtoToEntity(userSignUpDto.getUsername(),bCryptPasswordEncoder.encode(userSignUpDto.getPassword())
                ,userSignUpDto.getEmail(),userSignUpDto.getName()
                ,userSignUpDto.getAddressNumber(),userSignUpDto.getAddress()
                ,userSignUpDto.getDetailAddress(),userSignUpDto.getIsSeller(),userSignUpDto.getPhone());
        userRepository.save(user);
    }

    public User getUserByLoginId(String loginId) {
        return userRepository.findByLoginId(loginId).orElse(null);
    }
}
