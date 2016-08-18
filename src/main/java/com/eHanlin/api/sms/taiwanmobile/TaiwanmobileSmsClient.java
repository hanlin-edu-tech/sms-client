package com.eHanlin.api.sms.taiwanmobile;

import java.util.Map;

import com.eHanlin.api.sms.SmsClient;
import com.eHanlin.api.sms.util.Encoder;
import com.eHanlin.api.sms.util.HttpInvoker;

/**
 * 台灣大哥大簡訊服務
 */
public class TaiwanmobileSmsClient implements SmsClient {

    /**
     * 單則簡訊長度限制 70 字元
     */
    private final static int LENGTH_PER_SINGLE_MSG = 70;

    /**
     * 長簡訊由二至五則簡訊組成，每則(不含標頭)限制 67字元
     */
    private final static int LENGTH_PER_LONG_MSG = 67;

    /**
     * 長簡訊標頭格式：六個位元組的16進位表示(URI Encoded String)
     */
    private final static String HEADER_TEMPLATE = "%%05%%00%%03%%%1$02X%%%2$02d%%%3$02d";

    /**
     * 簡訊編碼
     */
    private final static String MSG_ENC = "BIG5";

    private String singleMessageApiUrl;

    private String longMessageApiUrl;

    private HttpInvoker httpInvoker;

    public TaiwanmobileSmsClient(String api, int port) {
        singleMessageApiUrl = String.format(api, port);
        longMessageApiUrl = String.format(api, port + 1);
        httpInvoker = new HttpInvoker();
    }

    @Override
    public Map send(String mobile, String message) {


        if (message.length() > LENGTH_PER_SINGLE_MSG) {
            return send(longMessageApiUrl, mobile, "L" + MSG_ENC, encodeAsLongMessage(message));
        } else {
            return send(singleMessageApiUrl, mobile, MSG_ENC, encodeMessage(message));
        }
    }

    /**
     * 發送訊息至台哥大
     */
    private Map send(String endpoint, String dstaddr, String encoding, String smbody) {
        String api = new StringBuilder(endpoint)
                            .append("&dstaddr=").append(dstaddr)
                            .append("&encoding=").append(encoding)
                            .append("&smbody=").append(smbody).toString();

        return httpInvoker.get(api);
    }

    /**
     * 編碼訊息
     * @param message
     * @return 經過 URI 編碼後的值，如果編碼過程發生例外錯誤，則回傳原訊息
     */
    private String encodeMessage(String message) {
        return Encoder.enc(message, MSG_ENC);
    }

    /**
     * 編碼為長簡訊
     * @param message
     * @return
     */
    private String encodeAsLongMessage(String message) {
        int random = (int)(Math.random() * 256);
        StringBuilder smbody = new StringBuilder();
        String[] parts = splitByLength(message, LENGTH_PER_LONG_MSG);
        for (int i = 0; i < parts.length; i++) {
            smbody
                .append(buildMessageHeader(random, parts.length, i + 1))
                .append(encodeMessage(parts[i]));
        }

        return smbody.toString();
    }

    /**
     * 長簡訊訊息切割
     * @param message
     * @param len
     * @return
     */
    private String[] splitByLength(String message, int len) {
        int parts = (int) Math.ceil(message.length() / (double) len);
        String[] messageParts = new String[parts];
        for (int i = 0; i < parts - 1; i++) {
            int begin = i * len;
            int end = begin + len;
            messageParts[i] = message.substring(begin, end);
        }
        messageParts[parts - 1] = message.substring((parts - 1) * len);
        return messageParts;
    }

    /**
     * 長簡訊標頭
     * @param random 隨機數字 (1 ~ 255)
     * @param total  總訊息數
     * @param part   第幾封   (1 ~ total)
     * @return
     */
    private String buildMessageHeader(int random, int total, int part) {
        return String.format(HEADER_TEMPLATE, random, total, part);
    }

}

