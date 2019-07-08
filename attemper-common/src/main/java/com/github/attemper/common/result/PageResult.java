package com.github.attemper.common.result;

import lombok.ToString;

@ToString
public class PageResult {

    /**
     * current page,starts with 1
     */
    protected int currentPage;

    /**
     * size of page
     */
    protected int pageSize;

    /**
     * total count
     */
    protected long total;

    public int getCurrentPage() {
        return currentPage;
    }

    public PageResult setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
        return this;
    }

    public int getPageSize() {
        return pageSize;
    }

    public PageResult setPageSize(int pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    public long getTotal() {
        return total;
    }

    public PageResult setTotal(long total) {
        this.total = total;
        return this;
    }
}
