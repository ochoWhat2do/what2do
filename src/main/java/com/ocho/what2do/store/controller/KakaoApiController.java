package com.ocho.what2do.store.controller;

import com.ocho.what2do.store.service.KakaoApiServiceImpl;
import com.ocho.what2do.store.dto.StoreListResponseDto;
import com.ocho.what2do.store.util.KakaoUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/daum")
@Tag(name = "카카오 API", description = "카카오 로컬 API 를 이용한 실시간 조회 기능을 담고 있습니다.")
public class KakaoApiController {

    private final KakaoApiServiceImpl kakaoApiService;
    private final KakaoUtil kakaoUtil;

    @Operation(summary = "카카오 API 를 이용한 실시간 검색", description = "query(검색어)와, page(페이징, 최대 3)을 통해 실시간으로 데이터를 조회하고 DB에 저장하여 값을 리스트의 형태로 반환합니다.")
    @GetMapping("/search")
    public StoreListResponseDto searchItems(@RequestParam String query,
                                            @RequestParam String page) {
        String result = kakaoUtil.callApi(query, page);
        return kakaoApiService.fromJSONtoItems(result);
    }
}