package com.ocho.what2do.common.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum CustomErrorCode {
  // 필요한 공통 메시지를 추가할 것
  USER_NOT_FOUND(HttpStatus.BAD_REQUEST.value(), "존재하지 않는 사용자입니다."),
  PASSWORD_RECENTLY_USED(HttpStatus.BAD_REQUEST.value(), "기존에 사용된 적 있는 비밀번호입니다."),
  OLD_PASSWORD_MISMATCHED(HttpStatus.BAD_REQUEST.value(), "기존 비밀번호와 일치하지 않습니다."),
  NEW_PASSWORD_MISMATCHED(HttpStatus.BAD_REQUEST.value(), "새 비밀번호가 일치하지 않습니다."),
  USER_ALREADY_EXISTS(HttpStatus.BAD_REQUEST.value(), "이미 존재하는 사용자입니다."),
  UNAUTHORIZED_REQUEST(HttpStatus.UNAUTHORIZED.value(), "승인되지 않은 요청입니다."),
  STORE_NOT_FOUND(HttpStatus.BAD_REQUEST.value(),"존재하지 않은 가게입니다."),
  NOT_EXIST_REFRESH_JWT(HttpStatus.BAD_REQUEST.value(), "존재하지 않거나 만료된 Refresh 토큰입니다. 다시 로그인해주세요."),
  ILLEGAL_JSON_STRING_EXCEPTION(HttpStatus.BAD_REQUEST.value(), "존재하지 않거나 만료된 Refresh 토큰입니다. 다시 로그인해주세요."),
  EXPIRED_BLACK_ACCESS_TOKEN_EXCEPTION(HttpStatus.BAD_REQUEST.value(), "만료되어 접속할 수 없는 토큰입니다. 다시 로그인해주세요."),
  COMMENT_ALREADY_LIKED(HttpStatus.BAD_REQUEST.value(),"이미 존재하는 좋아요 입니다."),
  COMMENT_NOT_LIKED(HttpStatus.BAD_REQUEST.value(),"존재하지 않는 좋아요 입니다." ),
  REVIEW_ALREADY_EXIST(HttpStatus.BAD_REQUEST.value(),"이미 존재하는 리뷰 입니다." ),
  REVIEW_NOT_FOUND(HttpStatus.BAD_REQUEST.value(),"리뷰가 존재하지 않습니다."),
  COMMENT_NOT_FOUND(HttpStatus.BAD_REQUEST.value(),"댓글이 존재하지 않습니다."),
  REVIEW_ALREADY_LIKED(HttpStatus.BAD_REQUEST.value(),"이미 존재하는 좋아요 입니다."),
  REVIEW_NOT_LIKED(HttpStatus.BAD_REQUEST.value()," 이미 좋아요를 누른 리뷰입니다." ),
  STORE_FAVORITE_ALREADY_EXIST(HttpStatus.BAD_REQUEST.value(),"이미 찜한 가게입니다."),
  STORE_FAVORITE_NOT_FOUND(HttpStatus.BAD_REQUEST.value(), "해당 가게에 취소할 찜이 없습니다."),
  NOT_ADMIN_AUTH(HttpStatus.BAD_REQUEST.value(),"관리자 권한이 없습니다."),
  HAVE_NOT_USER_ROLE(HttpStatus.BAD_REQUEST.value(),"유효한 사용자 권한이 아닙니다."),
  LOGIN_USER_ACCOUNT_LOCKED(HttpStatus.BAD_REQUEST.value(),"계정이 정지되었습니다. 관리자에게 문의하세요."),
  DATA_NOT_FOUND(HttpStatus.BAD_REQUEST.value(), "데이터가 존재하지 않습니다."),
  ;

  private final int errorCode;
  private final String errorMessage;
}
