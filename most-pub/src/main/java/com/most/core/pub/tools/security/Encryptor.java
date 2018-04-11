package com.most.core.pub.tools.security;


import java.math.BigInteger;
import java.security.MessageDigest;

public class Encryptor {

    public static String encryptMd5(String text) throws Exception{
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(text.getBytes());
        return new BigInteger(1, md.digest()).toString(32);
    }
}
