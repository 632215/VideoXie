package com.weis.videoxie.http;

public interface OnRequestListener<T> {
    void onSuccess(T t);

    void onError(String Code, String Msg);
}