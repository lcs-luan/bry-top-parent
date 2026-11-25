package com.bry.framework.base.config;

/**
 * 功能描述： <br/>
 *
 * @author LuoCS
 * @date 2025/4/11 0011
 */
public class SystemCodeConts {

    /**
     * 系统码：三位数字，范围（100开始至999）
     * 从nacos中读取，配置项：system.response.code
     * 规则：https://wiki.ds1688.com/pages/viewpage.action?pageId=18157038
     */
    private static String SYSTEM_CODE = "xxx";

    /**
     * 系统码最小值
     */
    public static final int MIN_SYSTEM_CODE = 100;

    /**
     * 系统码最大值
     */
    public static final int MAX_SYSTEM_CODE = 999;

    static void fillSystemCode(String systemCode) {
        SYSTEM_CODE = systemCode;
    }

    public static String getSystemCode() {
        return SYSTEM_CODE;
    }
}
