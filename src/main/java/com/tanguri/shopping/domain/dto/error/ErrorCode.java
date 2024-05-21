package com.tanguri.shopping.domain.dto.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    //사용할 ErroCode를 정의한다.
    //Status,code,message를 정의한다.
    //이 ErrorCode를 담을 CustomException을 봐라.
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "ACCOUNT-001", "사용자를 찾을 수 없습니다."),
    HAS_EMAIL(HttpStatus.BAD_REQUEST, "ACCOUNT-002", "존재하는 이메일입니다."),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "ACCOUNT-003", "비밀번호가 일치하지 않습니다."),
    PRODUCT_OWNER(HttpStatus.BAD_REQUEST,"ACCOUNT-004","본인의 상품만 수정,삭제가 가능합니다."),
    HAS_LOGIN_ID(HttpStatus.BAD_REQUEST,"ACCOUNT-005","해당 ID가 중복됩니다."),
    HAS_NICKNAME(HttpStatus.BAD_REQUEST,"ACCOUNT-006","닉네임이 중복됩니다."),
    STOCK_LACK(HttpStatus.BAD_REQUEST,"STOCK-001","상품 재고가 부족합니다."),
    MONEY_LACK(HttpStatus.BAD_REQUEST,"MONEY-001","잔고가 부족합니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
