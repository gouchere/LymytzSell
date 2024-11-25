package com.lymytz.lymytzsell.business.helpers;

import lombok.Data;

@Data
public class PaginatorProperties {
    private int position;
    private int pageSize;
    private int totalPages;
}
