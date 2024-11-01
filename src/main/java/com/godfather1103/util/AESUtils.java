package com.godfather1103.util;


import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * <p>Title:        Godfather1103's Github</p>
 * <p>Copyright:    Copyright (c) 2024</p>
 * <p>Company:      https://github.com/godfather1103</p>
 * 类描述：
 *
 * @author 作者: Jack Chu E-mail: chuchuanbao@gmail.com
 * @version 1.0
 * @date 创建时间：2024/11/1 18:17
 * @since 1.0
 */
public class AESUtils {

    /**
     * 加密<BR>
     *
     * @param sSrc 参数
     * @param sKey 参数
     * @return 结果
     * @throws Exception 异常
     * @author 作者: Jack Chu E-mail: chuchuanbao@gmail.com
     * @date 创建时间：2024/11/1 18:14
     */
    public static String encrypt(String sSrc, String sKey) throws Exception {
        if (StringUtils.isEmpty(sKey)) {
            throw new Exception("密钥不能为空");
        }
        // 判断Key是否为16位
        if (sKey.length() != 16) {
            throw new Exception("密钥长度错误");
        }
        byte[] raw = sKey.getBytes(StandardCharsets.UTF_8);
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        //"算法/模式/补码方式"
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] encrypted = cipher.doFinal(sSrc.getBytes(StandardCharsets.UTF_8));
        //此处使用BASE64做转码功能，同时能起到2次加密的作用。
        return java.util.Base64.getEncoder().encodeToString(encrypted);
    }

    /**
     * 解密<BR>
     *
     * @param sSrc 参数
     * @param sKey 参数
     * @return 结果
     * @throws Exception 异常
     * @author 作者: Jack Chu E-mail: chuchuanbao@gmail.com
     * @date 创建时间：2024/11/1 18:15
     */
    public static String decrypt(String sSrc, String sKey) throws Exception {
        // 判断Key是否正确
        if (StringUtils.isEmpty(sKey)) {
            throw new Exception("密钥不能为空");
        }
        // 判断Key是否为16位
        if (sKey.length() != 16) {
            throw new Exception("密钥长度错误");
        }
        byte[] raw = sKey.getBytes(StandardCharsets.UTF_8);
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);
        //先用base64解密
        byte[] encrypted1 = Base64.getDecoder().decode(sSrc);
        byte[] original = cipher.doFinal(encrypted1);
        return new String(original, StandardCharsets.UTF_8);
    }
}
