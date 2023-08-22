package com.ocho.what2do.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL) //응답값으로 변환이 될 때 제이슨 형태로 변환됨. 그때 NON_NULL인 얘들만 포함시킴.
public class ApiResponseDto {
  private int statusCode;
  private String statusMessage;

  public ApiResponseDto(int statusCode, String statusMessage) {
    this.statusCode = statusCode;
    this.statusMessage = statusMessage;
  }
}

