package com.fjx.arms.http;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author guanzhirui
 * @date 2022/8/1 14:44
 */
public interface GlobalHttpHandler {

    @NonNull
    Response onHttpResultResponse(@Nullable String httpResult, @NonNull Interceptor.Chain chain, @NonNull Response response);

    @NonNull
    Request onHttpRequestBefore(@NonNull Interceptor.Chain chain, @NonNull Request request);

}
