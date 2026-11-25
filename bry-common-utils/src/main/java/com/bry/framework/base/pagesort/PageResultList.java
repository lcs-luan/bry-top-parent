package com.bry.framework.base.pagesort;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;
import java.util.List;

/**
 * 分页查询结果
 *
 * @author: zeminsong
 * @Description:
 * @Date: create in 2020/11/3 18:28
 */
@Schema(description = "分页查询结果")
public class PageResultList<T> implements Serializable {
    private static final long serialVersionUID = 7812556364757470645L;

    @Schema(description = "列表数据")
    private List<T> listData;

    @Schema(description = "总数", type = "int", requiredMode = Schema.RequiredMode.REQUIRED, example = "7")
    private int totalCount;

    @Schema(description = "备用对象(可空)")
    private Object extend;

    public List<T> getListData() {
        return listData;
    }

    public void setListData(List<T> listData) {
        this.listData = listData;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public Object getExtend() {
        return extend;
    }

    public void setExtend(Object extend) {
        this.extend = extend;
    }
}
