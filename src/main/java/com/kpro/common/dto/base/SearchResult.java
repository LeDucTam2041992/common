package com.kpro.common.dto.base;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchResult <T> {
    private java.util.List<T> content;
    private int totalPages;
    private long totalElements;
    private boolean last;
    private int size;
    private boolean first;
    private int number;
    private int numberOfElements;
}
