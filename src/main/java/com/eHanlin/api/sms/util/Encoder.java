package com.eHanlin.api.sms.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * 網址編碼工具
 */
public class Encoder {

    public static String enc(String str, String charset) {
        try {
            return URLEncoder.encode(str, charset).replaceAll("\\+", "%20");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e.getClass().getName() + " : " + e.getMessage());
        }
    }

}

