package com.tanguri.shopping.service;

import com.tanguri.shopping.domain.entity.User;
import com.tanguri.shopping.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class LoginService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public User login(String username, String password){
        System.out.println("password = " + password);
        Optional<User> user = userRepository.findByLoginId(username);
        if(bCryptPasswordEncoder.matches(password,user.get().getPassword())){
            return user.get();
        }
        else {
            return null;
        }
    }
}
