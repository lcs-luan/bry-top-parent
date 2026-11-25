package com.bry.framework.base.code;

/**
 * 功能：错误码接口<br/>
 * 描述：错误码定义：https://wiki.ds1688.com/pages/viewpage.action?pageId=18157038
 *
 * @author LuoCS
 * @date 2025/4/18 0018
 */
public interface ErrorCodeInterface {
    /**
     * 通用异常码最小值：0
     */
    int BASE_MIN = 0;

    /**
     * 通用异常码最大值：99
     */
    int BASE_MAX = 99;

    /**
     * 业务异常码最小值：100
     */
    int BUSS_MIN = 100;

    /**
     * 业务异常码最大值：999
     */
    int BUSS_MAX = 999;

    /**
     * 错误码，范围：0-99公共，100-999业务 (例如：1)
     * https://wiki.ds1688.com/pages/viewpage.action?pageId=18157038
     */
    int getErrorCode();

    /**
     * 错误码-String格式(例如：001)
     */
    String getErrorCodeStr();

    /**
     * 错误信息描述
     */
    String getErrorMsg();
}
