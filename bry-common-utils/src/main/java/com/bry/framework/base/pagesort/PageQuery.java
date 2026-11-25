package com.bry.framework.base.pagesort;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;

/**
 * 分页查询参数
 *
 * @author: zeminsong
 * @Description:
 * @Date: create in 2020/11/1 13:12
 */
@Schema(description = "分页查询参数")
public class PageQuery implements Serializable {
    private static final long serialVersionUID = -6610123130146415533L;

    @Schema(description = "页码，从1开始")
    private int pageIndex;

    @Schema(description = "每页大小")
    private int pageSize;

    @Schema(description = "分页开始位置", hidden = true)
    @JsonIgnore
    private int start;

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getStart() {
        if (pageIndex <= 1) {
            return 0;
        }
        return (pageIndex - 1) * pageSize;
    }

    public PageQuery() {
        this.start = 0;
        this.pageIndex = 1;
        this.pageSize = 20;
    }
}
