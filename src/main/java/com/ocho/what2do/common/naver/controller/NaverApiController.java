package com.ocho.what2do.common.naver.controller;

import com.ocho.what2do.common.naver.dto.NaverDto;
import com.ocho.what2do.common.naver.service.NaverApiServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/naver")
public class NaverApiController {

    private final NaverApiServiceImpl naverApiServiceImpl;

    @GetMapping("/search")
    public List<NaverDto> searchItems(@RequestParam String query) {
        return naverApiServiceImpl.searchItems(query);
    }
}
