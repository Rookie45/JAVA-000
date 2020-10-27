package com.sl.java00.week02;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class CustomOkHttp {
    private static final String URL = "http://localhost:8801";
    private static final OkHttpClient HTTP_CLIENT = new OkHttpClient.Builder()
            .connectTimeout(5L, TimeUnit.SECONDS)
            .readTimeout(5L, TimeUnit.SECONDS)
            .build();

    public static void main(String[] args) {
        System.out.println(httpGet(URL));
    }

    public static String httpGet(String url) {
        String response = null;
        if (null == url) {
            return response;
        }
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        response = execute(request);
        return response;
    }

    private static String execute(Request request) {
        String responseResult = null;
        try (Response response = HTTP_CLIENT.newCall(request).execute()){
            if (null == response) {
                return responseResult;
            }
            if (response.isSuccessful() && null != response.body()) {
                responseResult =response.body().string();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return responseResult;
    }
}
