package com.eHanlin.api.sms;

import java.util.Map;

public interface SmsClient {

    /**
     * 發送手機訊息
     * @param mobile 目標手機號碼
     * @param message 訊息
     * @return 簡訊寄送的結果
     */
    Map send(String mobile, String message);

}

