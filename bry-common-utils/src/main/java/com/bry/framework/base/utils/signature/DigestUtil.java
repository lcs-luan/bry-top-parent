package com.bry.framework.base.utils.signature;

import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 加密工具
 *
 * @author luochengsong
 * @date 2021/4/15 0015
 */
public class DigestUtil {

    public static final String ALGORITHM_MD5 = "MD5";
    public static final String ALGORITHM_SHA1 = "SHA-1";
    public static final String ALGORITHM_SHA256 = "SHA-256";
    public static final String ALGORITHM_SHA512 = "SHA-512";

    private DigestUtil() {
    }

    /**
     * 对UTF-8编码的字符串加密
     *
     * @author: lcs
     * @Date: 上午 9:24 2021/4/19 0019
     */
    public static String encode(String originTxt, String encodingAlgorithm) {
        return encode(originTxt, encodingAlgorithm, null);
    }

    public static String encode(String originTxt, String encodingAlgorithm, String charsetEncoding) {
        if (originTxt == null) {
            return null;
        } else {
            try {
                MessageDigest messageDigest = MessageDigest.getInstance(encodingAlgorithm);
                if (!StringUtils.isEmpty(charsetEncoding)) {
                    messageDigest.update(originTxt.getBytes(charsetEncoding));
                } else {
                    messageDigest.update(originTxt.getBytes(StandardCharsets.UTF_8));
                }

                byte[] digest = messageDigest.digest();
                return HexUtil.bytesToHex(digest);
            } catch (NoSuchAlgorithmException e1) {
                throw new SecurityException(e1);
            } catch (UnsupportedEncodingException e2) {
                throw new SecurityException(e2);
            }
        }
    }

    public static String md5(String origin) {
        return encode(origin, ALGORITHM_MD5);
    }

    public static String sha1(String origin) {
        return encode(origin, ALGORITHM_SHA1);
    }

    public static String sha256(String origin) {
        return encode(origin, ALGORITHM_SHA256);
    }

    public static String sha512(String origin) {
        return encode(origin, ALGORITHM_SHA512);
    }
}
