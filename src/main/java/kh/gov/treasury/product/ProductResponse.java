package kh.gov.treasury.product;

import lombok.Builder;

@Builder
public record ProductResponse <T> (
    int number,
    int size,
    long totalElements,
    int totalPages,
    int first,
    int last,
    T products,
    T prdSources,
    T prdNames,
    T prdCats
) {
}