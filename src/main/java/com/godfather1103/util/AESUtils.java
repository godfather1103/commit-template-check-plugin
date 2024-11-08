package com.godfather1103.util;


import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Set;

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

    private static final String ENC_KEY = "b2849e29970d4285bfc21f726c00eaeb";
    private static final String IV = "1qaz!@#$2wsx%^&*";
    private static final String ALGORITHM = "AES";
    private static final String PADDING = "AES/CBC/PKCS5Padding";
    private static final Set<Integer> KEY_LENGTH = Set.of(16, 24, 32);

    /**
     * 加密<BR>
     *
     * @param sSrc 参数
     * @return 结果
     * @throws Exception 异常
     * @author 作者: Jack Chu E-mail: chuchuanbao@gmail.com
     * @date 创建时间：2024/11/1 18:14
     */
    public static String encrypt(String sSrc) throws Exception {
        return encrypt(sSrc, ENC_KEY);
    }

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
        // 判断Key
        if (!KEY_LENGTH.contains(sKey.length())) {
            throw new Exception("密钥长度错误");
        }
        SecretKeySpec skeySpec = new SecretKeySpec(sKey.getBytes(StandardCharsets.UTF_8), ALGORITHM);
        //"算法/模式/补码方式"
        Cipher cipher = Cipher.getInstance(PADDING);
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, new IvParameterSpec(IV.getBytes(StandardCharsets.UTF_8)));
        byte[] encrypted = cipher.doFinal(sSrc.getBytes(StandardCharsets.UTF_8));
        //此处使用BASE64做转码功能，同时能起到2次加密的作用。
        return Base64.getEncoder().encodeToString(encrypted);
    }

    /**
     * 解密<BR>
     *
     * @param sSrc 参数
     * @return 结果
     * @throws Exception 异常
     * @author 作者: Jack Chu E-mail: chuchuanbao@gmail.com
     * @date 创建时间：2024/11/1 18:15
     */
    public static String decrypt(String sSrc) throws Exception {
        return decrypt(sSrc, ENC_KEY);
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
        // 判断Key
        if (!KEY_LENGTH.contains(sKey.length())) {
            throw new Exception("密钥长度错误");
        }
        SecretKeySpec skeySpec = new SecretKeySpec(sKey.getBytes(StandardCharsets.UTF_8), ALGORITHM);
        Cipher cipher = Cipher.getInstance(PADDING);
        cipher.init(Cipher.DECRYPT_MODE, skeySpec, new IvParameterSpec(IV.getBytes(StandardCharsets.UTF_8)));
        //先用base64解密
        byte[] encrypted = Base64.getDecoder().decode(sSrc);
        byte[] original = cipher.doFinal(encrypted);
        return new String(original, StandardCharsets.UTF_8);
    }
}
