package com.bry.framework.base.enums;

import org.springframework.util.StringUtils;

/**
 * 功能： 排序方式<br/>
 *
 * @author LuoCS
 * @date 2021/11/17 0017
 */
public enum SortTypeEnum {
    ASC, DESC;

    /**
     * 根据排序类型判断排序是否存在
     *
     * @param sortType
     * @return com.bry.framework.common.enums.SortTypeEnum
     */
    public static SortTypeEnum indexOf(String sortType) {
        if (StringUtils.isEmpty(sortType)) {
            return null;
        }
        sortType = sortType.toUpperCase();
        for (SortTypeEnum sortTypeEnum : SortTypeEnum.values()) {
            if (sortTypeEnum.name().equals(sortType)) {
                return sortTypeEnum;
            }
        }
        return null;
    }
}
