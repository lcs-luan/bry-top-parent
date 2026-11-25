package com.bry.framework.base.pagesort;

import com.bry.framework.base.enums.SortTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.util.StringUtils;

/**
 * 功能： 字段排序实体<br/>
 * 描述：
 *
 * @author LuoCS
 * @date 2021/11/17 0017
 */
@Data
@Schema(description = "字段排序实体")
public class ColumnSort {
    /**
     * 排序字段名称
     */
    @Schema(description = "排序字段", example = "refundTime")
    private String sortColumn;

    /**
     * 排序方式
     */
    @Schema(description = "排序方式(asc:正序，desc:倒序)", example = "desc")
    private String sortType;

    public boolean valid() {
        if (StringUtils.isEmpty(sortColumn)) {
            return false;
        }
        return SortTypeEnum.indexOf(sortType) != null;
    }

}
