package com.ocho.what2do.admin.dto;

import com.ocho.what2do.common.file.S3FileDto;
import java.util.List;
import lombok.Builder;

public class AdminApiStoreRequestDto {

 private boolean locked;
 private List<S3FileDto> images;

  @Builder
  public AdminApiStoreRequestDto(boolean locked, List<S3FileDto> images) {
    this.locked = locked;
    this.images = images;
  }
}
