package com.group7.straytostay.model;

import java.util.Collections;
import java.util.List;

/**
 * One page of results plus the numbers the view needs to draw a pager.
 */
public final class PageResult<T> {

    private final List<T> items;
    private final int page;
    private final int pageSize;
    private final int total;

    public PageResult(List<T> items, int page, int pageSize, int total) {
        this.items = items == null ? Collections.emptyList() : List.copyOf(items);
        this.page = page;
        this.pageSize = pageSize;
        this.total = total;
    }

    public List<T> getItems() { return items; }
    public int getPage() { return page; }
    public int getPageSize() { return pageSize; }
    public int getTotal() { return total; }

    public int getTotalPages() {
        if (total == 0) return 1;
        return (total + pageSize - 1) / pageSize;
    }

    public boolean isHasPrevious() { return page > 1; }
    public boolean isHasNext() { return page < getTotalPages(); }
    public boolean isEmpty() { return items.isEmpty(); }
}
