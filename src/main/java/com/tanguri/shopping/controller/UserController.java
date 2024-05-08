package com.tanguri.shopping.controller;

import com.tanguri.shopping.domain.dto.ResponseDto;
import com.tanguri.shopping.domain.dto.user.CustomUserDetails;
import com.tanguri.shopping.domain.dto.user.UserLoginDto;
import com.tanguri.shopping.domain.dto.user.UserSignUpDto;
import com.tanguri.shopping.domain.entity.User;
import com.tanguri.shopping.service.LoginService;
import com.tanguri.shopping.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final LoginService loginService;

    @GetMapping("/")
    public String mainPage(Model model,@AuthenticationPrincipal CustomUserDetails customUserDetails){
        if(customUserDetails!=null){
            model.addAttribute("user", customUserDetails.getUserEntity());
        }
        return "home/home";
    }

    @GetMapping("/user/signup")
    public String SignUpPage(@ModelAttribute("user")UserSignUpDto userSignUpDto){
        return "user/usersignup";
    }
    @PostMapping("/user/signup")
    public String PostSignUp(@Validated @ModelAttribute("user")UserSignUpDto userSignUpDto, BindingResult bindingResult){
        if (!userSignUpDto.getPassword().equals(userSignUpDto.getPasswordCheck())) {
            bindingResult.rejectValue("passwordCheck","passwordCheckValid","비밀번호가 일치하지 않습니다.");
        }
        if (bindingResult.hasErrors()) {
            return "user/usersignup";
        }
        userService.saveUser(userSignUpDto);
        return "redirect:/";
    }
    @GetMapping("/user/login")
    public String LoginPage(@ModelAttribute("user") UserLoginDto userLoginDto){
        return "user/userlogin";
    }

    @PostMapping("/user/login")
    public String PostLogin(@Validated @ModelAttribute("user") UserLoginDto userLoginDto,BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "user/userlogin";
        }
        User user = loginService.login(userLoginDto.getUsername(), userLoginDto.getPassword());
        if(user==null){
            bindingResult.reject("loginFail","아이디 또는 비밀번호가 맞지 않습니다.");
            return "user/userlogin";
        }
        return "redirect:/";
    }
    @RequestMapping(value = "/user/signup/loginIdCheck")
    public @ResponseBody ResponseDto<?> check(@RequestBody(required = false) String loginId)  {
        if(loginId==null || loginId.isEmpty()){
            return new ResponseDto<>(-1,"아이디를 입력해주세요.",null);
        }
        if(!loginId.matches("^[a-z0-9]+$") || loginId.length()<4 || loginId.length()>10) {
            return new ResponseDto<>(-1, "아이디는 알파벳 소문자와 숫자만 포함할 수 있습니다. 4글자 이상 10글자 이하로 입력해주세요.", null);
        }
        User user = userService.getUserByLoginId(loginId);
        if(user==null){
            return new ResponseDto<>(1,"사용 가능한 ID입니다.",true);
        }
        else {
            return new ResponseDto<>(1,"중복된 아이디입니다.",false);
        }
    }
}
