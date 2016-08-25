package com.eHanlin.api.sms.util;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * HTTP 呼叫工具
 */
public class HttpInvoker {

    private static final String RESPONSE_LINE_SPLITTER = "\\r\\n|\\r|\\n";

    private static final String RESPONSE_TOKEN_SPLITTER = "=";

    private HttpClient httpClient;

    public HttpInvoker() {
        httpClient = new DefaultHttpClient();
    }

    public Map get(String api) {
        Map<String, String> result;
        HttpGet request = new HttpGet(api);
        try {
            String responseString = EntityUtils.toString(httpClient.execute(request).getEntity());
            result = responseStringToMap(responseString);

        } catch (Exception e) {
            result = exceptionToMap(e);

        } finally {
            request.abort();
        }

        return result;
    }

    public static Map<String, String> responseStringToMap(String responseString) {
        Map<String, String> result = new HashMap<>();
        String[] tokens = responseString.split(RESPONSE_LINE_SPLITTER);
        for (int i = 0; i < tokens.length; i++) {
            String token = tokens[i];
            String[] keyVal = token.split(RESPONSE_TOKEN_SPLITTER);
            if (keyVal.length == 2) {
                result.put(keyVal[0], keyVal[1]);
            } else {
                result.put("token" + i, token);
            }
        }

        return result;
    }

    public static Map<String, String> exceptionToMap(Exception e) {
        Map<String, String> result = new HashMap<>();
        result.put("exception", e.getClass().getName());
        result.put("message", e.getMessage());
        return result;
    }

}

