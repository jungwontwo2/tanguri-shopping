package com.tanguri.shopping.domain.dto.user;

import com.tanguri.shopping.domain.entity.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserSignUpDto {
    @NotBlank(message = "아이디를 입력하세요")
    @Size(min = 4,max = 10,message = "최소 4자 이상, 10자 이하로 입력하세요")
    @Pattern(regexp = "^[a-z0-9]*$", message = "알파벳 소문자(a~z), 숫자(0~9)만 입력 가능합니다.")
    private String username;
    @NotBlank(message = "비밀번호를 입력하세요")
    @Size(min = 8,max = 20,message = "최소 8자 이상, 20자 이하로 입력하세요")
    @Pattern(regexp = "^[a-zA-Z0-9]*$", message = "알파벳 대소문자(a~z, A~Z), 숫자(0~9)만 입력 가능합니다.")
    private String password;
    @NotBlank(message = "비밀번호를 입력하세요")
    @Size(min = 8,max = 20,message = "최소 8자 이상, 20자 이하로 입력하세요")
    @Pattern(regexp = "^[a-zA-Z0-9]*$", message = "알파벳 대소문자(a~z, A~Z), 숫자(0~9)만 입력 가능합니다.")
    private String passwordCheck;

    @NotBlank(message = "이메일을 입력하세요")
    @Size(min = 8,max = 20,message = "최소 8자 이상, 20자 이하로 입력하세요")
    @Pattern(regexp = "^[0-9a-zA-Z]([-_\\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\\.]?[0-9a-zA-Z])*\\.[a-zA-Z]{2,3}",
            message = "올바르지 않은 이메일 형식입니다.")
    private String email;

    @NotBlank(message = "이름을 입력하세요")
    @Size(min = 2,max = 10,message = "최소 2자 이상, 10자 이하로 입력하세요")
    @Pattern(regexp = "^[가-힣]+$", message = "한글만 입력 가능합니다.")
    private String name;

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

    @NotNull(message = "판매자나 구매자를 선택해주세요")
    private Boolean isSeller;


    public static User SingUpDtoToEntity(String username,String password,String email,String name,
                                         String addressNumber,String address, String detailAddress,
                                         boolean isSeller,String phone){
        return new User(username,password,email, name, addressNumber,address, detailAddress,isSeller, phone);
    }
}
