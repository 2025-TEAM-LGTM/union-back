package com.union.demo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class DropDownPersonalityResDto {
    private List<ItemDto> items;

    @Getter
    @Builder
    @AllArgsConstructor
    public static class ItemDto {
        private String key;
        private String label;
        private OptionDto first;
        private OptionDto second;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class OptionDto {
        private int code;
        private String name;
    }
}