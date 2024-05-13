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
    @PostConstruct
    public void dataInit(){
        UserSignUpDto userSignUpDto = new UserSignUpDto("asdf", "asdfasdf",
                "asdfasdf", "Tanguri@naver.com", "구링", "36435",
                "도안동로23", "372동 1923호", "01000000000", true);
        userService.saveUser(userSignUpDto);
//        Optional<User> optionalUser = userRepository.findByLoginId("asdf");
//        User user = optionalUser.get();
        System.out.println("Init User Complete");
//        for (int i = 0; i < 20; i++) {
//            Content content = Content.builder()
//                    .title(Integer.toString(i+1))
//                    .texts(Integer.toString(i+1))
//                    .user(user)
//                    .password(user.getPassword())
//                    .build();
//            contentRepository.save(content);
//        }
//        System.out.println("Init content Complete");
    }

    //댓글 개수 초기화 작업
    //@PostConstruct
//    @Transactional
//    public void setCommentCnt(){
//        List<Content> contents = contentRepository.findAll();
//        for (Content content : contents) {
//            int commentCnt = content.getComments().size();
//            content.setCommentCnt(commentCnt);
//            contentRepository.save(content);
//        }
//    }

}
