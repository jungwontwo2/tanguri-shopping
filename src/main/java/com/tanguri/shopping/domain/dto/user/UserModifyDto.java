package com.tanguri.shopping.domain.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserModifyDto {
    @NotBlank(message = "이메일을 입력하세요")
    @Size(min = 8,max = 20,message = "최소 8자 이상, 20자 이하로 입력하세요")
    @Pattern(regexp = "^[0-9a-zA-Z]([-_\\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\\.]?[0-9a-zA-Z])*\\.[a-zA-Z]{2,3}",
            message = "올바르지 않은 이메일 형식입니다.")
    private String email;

    @NotBlank(message = "주소코드를 입력하세요")
    @Pattern(regexp = "^[0-9]+$", message = "숫자만 입력 가능합니다.")
    private String addressNumber;
    @NotBlank(message = "주소를 입력하세요")
    @Pattern(regexp = "^[가-힣0-9a-zA-Z\\s-.,()]+$",message = "숫자 한글 영어만 입력 가능합니다.")
    private String address;

    @NotBlank(message = "상세주소를 입력하세요")
    @Pattern(regexp = "^[가-힣0-9a-zA-Z\s]+$",message = "숫자 한글 영어만 입력 가능합니다.")
    private String detailAddress;

    @NotBlank(message = "전화번호를 입력하세요")
    @Pattern(regexp = "^01(0|1|[6-9])[0-9]{3,4}[0-9]{4}", message = "10 ~ 11 자리의 숫자만 입력 가능합니다.")
    private String phone;
}
