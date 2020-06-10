package com.hvs.diploma.util;

import com.hvs.diploma.components.SortAndFilterParams;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PageableHelper {
    private PageableHelper() {
    }

    public static int checkPageParameter(int page) {
        return Math.max(page, 0);
    }

    public static Pageable getPageable(int page, long count, int size, SortAndFilterParams params) {
        Sort.Direction direction = params.getSortOrder()
                .equalsIgnoreCase("Ascending") ? Sort.Direction.ASC : Sort.Direction.DESC;
        page = checkPageParam(page, count, size);
        return PageRequest.of(page, size, Sort.by(direction, params.getSortBy()));
    }

    public static int checkPageParam(int page, long count, int size) {
        long pagesCount = getPagesCount(count, size);
        if (page > pagesCount - 1) {
            page = (int) pagesCount - 1;
        }
        if (page < 0) {
            page = 0;
        }
        return page;
    }

    public static int getPagesCount(long count, int itemsPerPage) {
        return count % itemsPerPage == 0 ? (int) count / itemsPerPage : (int) count / itemsPerPage + 1;
    }

}
