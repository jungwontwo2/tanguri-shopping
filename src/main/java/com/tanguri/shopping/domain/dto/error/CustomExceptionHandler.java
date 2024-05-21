package com.tanguri.shopping.domain.dto.error;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@ControllerAdvice//컨트롤러 전역에서 발생하는 Custom Error를 잡아줄 Handler.
public class CustomExceptionHandler {

    @ExceptionHandler(CustomException.class)//우리가 만든 CustomException.class가 들어오면 @ExceptionHandler 실행
    public ModelAndView handleCustomException(CustomException e, HttpServletRequest request, HttpServletResponse response,
                                              Model model) throws IOException {
        model.addAttribute("code", e.getErrorCode().getCode());
        model.addAttribute("msg", e.getErrorCode().getMessage());
        model.addAttribute("timeStamp", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        // 이전 페이지의 URL을 모델에 추가
        String referer = request.getHeader("Referer");
        model.addAttribute("redirectUrl", referer != null ? referer : "/"); // 이전 페이지가 없으면 기본값으로 루트 경로 설정

        return new ModelAndView("common/messageRedirect");
    }
}
