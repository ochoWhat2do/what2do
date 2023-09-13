package com.ocho.what2do.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EditUserRequestDto {
    private String nickname;
    private String introduction;
    private String defaultPicture; // 이미지를 수정하지않고 그대로 프로필저장을하면 -> 값존재 , 이미지를 수정하면 값 빈값 (프론트에서 처리)
}
