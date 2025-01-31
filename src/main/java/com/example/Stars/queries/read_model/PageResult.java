package com.example.Stars.queries.read_model;

import java.util.List;

public class PageResult<T> {
    private final List<T> items;
    private final long totalItems;

    public PageResult(List<T> items, long totalItems) {
        this.items = items;
        this.totalItems = totalItems;
    }

    public List<T> getItems() {
        return items;
    }

    public long getTotalItems() {
        return totalItems;
    }
}

