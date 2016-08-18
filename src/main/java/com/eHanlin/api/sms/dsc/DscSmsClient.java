package com.eHanlin.api.sms.dsc;

import com.eHanlin.api.sms.SmsClient;
import com.eHanlin.api.sms.util.Encoder;
import com.eHanlin.api.sms.util.HttpInvoker;

import java.util.Map;

/**
 * 鼎新簡訊服務
 */
public class DscSmsClient implements SmsClient {

    /**
     * 簡訊編碼
     */
    private final static String MSG_ENC = "BIG5";

    private String endpoint;

    private HttpInvoker httpInvoker;

    public DscSmsClient(String endpoint) {
        this.endpoint = endpoint;
        httpInvoker = new HttpInvoker();
    }

    @Override
    public Map send(String mobile, String message) {
        String api = new StringBuilder(endpoint)
                .append("&dstaddr=").append(mobile)
                .append("&encoding=").append(MSG_ENC)
                .append("&smbody=").append(Encoder.enc(message, MSG_ENC)).toString();

        return httpInvoker.get(api);
    }

}

