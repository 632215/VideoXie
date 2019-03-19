package com.weis.videoxie.http;

import com.weis.videoxie.utils.LogUtils;

import java.io.IOException;

import okhttp3.Interceptor;

/**
 * 拦截器
 */
public class LogInterceptor implements Interceptor {

    @Override
    public okhttp3.Response intercept(Chain chain) throws IOException {
        okhttp3.Response response = chain.proceed(chain.request());
        okhttp3.MediaType mediaType = response.body().contentType();
        String content = response.body().string().trim();
        LogUtils.w("32ss:" + response.code() + "-------request content:" + "api:" + response.request().url());
        LogUtils.w("32ss:" + "content"+content);
        return response.newBuilder().body(okhttp3.ResponseBody.create(mediaType, content)).build();
    }
}
