package com.fuelpass.dto.response;

import java.util.List;

/**
 * Generic paginated response wrapper
 */
public class PaginatedResponse<T> {

    private List<T> data;
    private PaginationInfo pagination;

    // Constructors
    public PaginatedResponse() {}

    public PaginatedResponse(List<T> data, PaginationInfo pagination) {
        this.data = data;
        this.pagination = pagination;
    }

    // Getters and Setters
    public List<T> getData() { return data; }
    public void setData(List<T> data) { this.data = data; }

    public PaginationInfo getPagination() { return pagination; }
    public void setPagination(PaginationInfo pagination) { this.pagination = pagination; }

    /**
     * Pagination information
     */
    public static class PaginationInfo {
        private int page;
        private int limit;
        private long total;
        private int totalPages;
        private boolean hasNext;
        private boolean hasPrevious;

        // Constructors
        public PaginationInfo() {}

        public PaginationInfo(int page, int limit, long total, int totalPages) {
            this.page = page;
            this.limit = limit;
            this.total = total;
            this.totalPages = totalPages;
            this.hasNext = page < totalPages;
            this.hasPrevious = page > 0;
        }

        // Getters and Setters
        public int getPage() { return page; }
        public void setPage(int page) { this.page = page; }

        public int getLimit() { return limit; }
        public void setLimit(int limit) { this.limit = limit; }

        public long getTotal() { return total; }
        public void setTotal(long total) { this.total = total; }

        public int getTotalPages() { return totalPages; }
        public void setTotalPages(int totalPages) { this.totalPages = totalPages; }

        public boolean isHasNext() { return hasNext; }
        public void setHasNext(boolean hasNext) { this.hasNext = hasNext; }

        public boolean isHasPrevious() { return hasPrevious; }
        public void setHasPrevious(boolean hasPrevious) { this.hasPrevious = hasPrevious; }
    }
}