package com.bry.framework.base.utils.signature;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 签名和校验签名工具
 *
 * @author lcs
 * @version 1.0
 */
@Slf4j
public final class SignatureUtil {
    private SignatureUtil() {
    }

    public static final String SIGN = "sign";
    public static final String TIMESTAMP = "timestamp";

    /**
     * 校验签名是否有效。
     *
     * @param data      输入参数
     * @param secretKey 签名密钥
     * @return true 签名有效，false 签名无效
     */
    public static boolean validate(final Map<String, Object> data, final String secretKey) {
        if (data == null || data.isEmpty() || data.get(TIMESTAMP) == null) {
            log.info("timestamp is null!");
            return false;
        }
        if (!data.containsKey(SIGN) || data.get(SIGN) == null) {
            log.info("sign is null!");
            return false;
        }
        final Map<String, Object> tempData = new HashMap<>(data);
        tempData.remove(SIGN);
        final String newSign = getSign(tempData, secretKey);
        final String sign = data.get(SIGN).toString();
        return sign.equals(newSign);
    }

    /**
     * 对数据进行签名,且将签名后生成的sign放到data中
     *
     * @param data      待签名的数据
     * @param secretKey 签名密钥
     */
    public static void sign(final Map<String, Object> data, final String secretKey) {
        data.put(SIGN, getSign(data, secretKey));
    }

    /**
     * 获取签名字符串.MD5算法
     *
     * @param data      待签名的数据
     * @param secretKey 签名密钥
     * @return 签名字符串
     */
    private static String getSign(Map<String, Object> data, String secretKey) {
        //存储参与签名的转换成大写后的key
        List<String> upperParamKeyList = new ArrayList<>();
        //存储参与签名的数据
        Map<String, Object> signData = new HashMap<>();
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            Object value = entry.getValue();
            //1. data中的value值为null或者""的栏位不参与签名
            if (value != null && !"".equals(value.toString())) {
                String upperParamKey = entry.getKey().toUpperCase();
                upperParamKeyList.add(upperParamKey);
                signData.put(upperParamKey, value);
            }
        }
        //2. 按字典顺序排序
        Collections.sort(upperParamKeyList);
        //3. 将参与签名的数据拼接成字符串
        final StringBuilder builder = new StringBuilder();
        for (String upperParamKey : upperParamKeyList) {
            builder.append(upperParamKey).append("=").append(signData.get(upperParamKey)).append("&");
        }
        // 4. 拼接密钥
        builder.append(secretKey);
        //5. md5算法获取签名
        return DigestUtil.md5(builder.toString());
    }

}
