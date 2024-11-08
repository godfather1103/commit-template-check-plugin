package com.godfather1103.util;

import junit.framework.TestCase;

public class AESUtilsTest extends TestCase {

    public void testEncrypt() throws Exception {
        String info = "中华人民共和国🇨🇳";
        String enc = AESUtils.encrypt(info);
        System.out.println("加密结果:" + enc);
        System.out.println("解密结果:" + AESUtils.decrypt(enc));
    }

}