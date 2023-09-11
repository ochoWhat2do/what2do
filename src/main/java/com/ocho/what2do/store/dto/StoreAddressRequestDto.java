package com.ocho.what2do.store.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class StoreAddressRequestDto {
    @NotBlank
    private String address;
}
