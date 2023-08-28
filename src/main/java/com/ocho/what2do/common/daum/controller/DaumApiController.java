package com.ocho.what2do.common.daum.controller;

import com.ocho.what2do.common.daum.service.DaumApiServiceImpl;
import com.ocho.what2do.store.dto.StoreApiDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/daum")
public class DaumApiController {

    private final DaumApiServiceImpl daumApiServiceImpl;

    @GetMapping("/search")
    public List<StoreApiDto> searchItems(@RequestParam String query,
                                         @RequestParam String page,
                                         @RequestParam String size) {
        return daumApiServiceImpl.searchItems(query, page, size);
    }
}