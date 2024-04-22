package com.iqbal.blog.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class PagingResponse {

    private Integer size;

    private Integer page;

    private Integer totalPage;

    private Long totalElements;
}
