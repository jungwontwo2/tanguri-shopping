package com.tanguri.shopping;

import com.tanguri.shopping.domain.dto.user.UserSignUpDto;
import com.tanguri.shopping.repository.UserRepository;
import com.tanguri.shopping.service.UserService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class DataInit {
    private final UserService userService;
    private final UserRepository userRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    //테스트용 데이터 추가
    //@PostConstruct
    @PostConstruct
    public void dataInit(){
        UserSignUpDto userSignUpDto = new UserSignUpDto("asdf", "asdfasdf",
                "asdfasdf", "Tanguri@naver.com", "구링", "36435",
                "도안동로23", "372동 1923호", "01000000000", true);
        userService.saveUser(userSignUpDto);
        UserSignUpDto userSignUpDto2 = new UserSignUpDto("qwer", "qwerqwer",
                "qwerqwer", "Tanguri2@naver.com", "구링2", "36235",
                "도안동로13", "372동 1923호", "01000000000", false);
        userService.saveUser(userSignUpDto2);
        System.out.println("Init User Complete");
    }

}
