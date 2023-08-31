package com.ocho.what2do.admin.dto;

import lombok.Getter;

@Getter
public class UserLockRequestDto {
  private String email;
  private boolean locked;
}
