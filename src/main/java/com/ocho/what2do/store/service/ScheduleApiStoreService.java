package com.ocho.what2do.store.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public interface ScheduleApiStoreService {

  /**
   * resources 폴더 내의 업로드할 이미지 선정
   * @return List<File> 파일목록 반환
   */
  public List<File> getAllImageFiles() throws IOException;
    
    
  /**
   * 스케줄러를 통한 스토어 정보 업데이트(이미지)
   * @return 처리건수를 반환
   */
  public int UpdateApiStoreImages() throws IOException;
}
