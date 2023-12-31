package com.ocho.what2do.common.sceduler;

import com.ocho.what2do.store.service.ScheduleApiStoreServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j(topic = "StoreImage Scheduler")
@Component
@RequiredArgsConstructor
public class StoreImageScheduler {

  private final ScheduleApiStoreServiceImpl scheduleApiStoreService;

  // @Scheduled(cron = "* */10 * * * *", zone = "Asia/Seoul") //10 분마다
  // 초, 분, 시, 일, 월, 주 순서
  @Scheduled(cron = "0 0 6 * * *", zone = "Asia/Seoul") // 매일 새벽 6시
  public void yourScheduledMethod() {
    // 스케줄러가 실행될 로직
    log.info("가게 이미지 등록 스케줄러 실행");
    int result = 0;
    try {
      result = scheduleApiStoreService.UpdateApiStoreImages();
    } catch (IOException e) {
      throw new RuntimeException(e);
    } finally {
      if (result > 0) {
        log.info("가게 이미지 등록 스케줄러 성공");
      } else if (result == 0) {
        log.info("가게 이미지 등록된 건이 없습니다.");
      } else {
        log.error("가게 이미지 등록 스케줄러 실패");
      }
    }


  }

}
