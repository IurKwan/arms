package com.fjx.arms.di.module;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.fjx.arms.di.component.AppComponent;
import com.fjx.arms.http.GlobalHttpHandler;
import com.fjx.arms.http.factory.OkhttpEventListenerFactory;
import com.fjx.arms.log.RequestInterceptor;
import com.fjx.arms.rxerror.core.RxErrorHandler;
import com.fjx.arms.rxerror.handler.listener.ResponseErrorListener;
import com.fjx.arms.util.ArmUtil;
import com.google.gson.Gson;

import java.io.File;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.Dispatcher;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author guanzhirui
 * @date 2022/8/1 9:06
 */
@Module
public abstract class ClientModule {

    private static final int TIME_OUT = 5;

    @Singleton
    @Provides
    static Retrofit provideRetrofit(Application application, @Nullable RetrofitConfiguration configuration, Retrofit.Builder builder, OkHttpClient client
            , HttpUrl httpUrl, Gson gson) {
        builder.baseUrl(httpUrl)
                .client(client);

        if (configuration != null) {
            configuration.configRetrofit(application, builder);
        }

        builder.addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create());

        return builder.build();
    }

    @Singleton
    @Provides
    static OkHttpClient provideClient(Application application, @Nullable OkhttpConfiguration configuration, OkHttpClient.Builder builder, Interceptor intercept
            , @Nullable List<Interceptor> interceptors, @Nullable GlobalHttpHandler handler, ThreadPoolExecutor executorService) {
        builder
                .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
                .writeTimeout(TIME_OUT, TimeUnit.SECONDS)
                .readTimeout(TIME_OUT, TimeUnit.SECONDS)
                .addNetworkInterceptor(intercept);

        if (handler != null) {
            AppComponent appComponent = ArmUtil.obtainAppComponent(application);
            File cacheFile = new File(appComponent.cacheFile(), "okhttp");
            long maxSize = 50 * 1024 * 1024L;
            Cache cache = new Cache(cacheFile, maxSize);
            builder.eventListenerFactory(OkhttpEventListenerFactory.FACTORY)
                    .cache(cache)
                    .addInterceptor(chain -> chain.proceed(handler.onHttpRequestBefore(chain, chain.request())));
        }

        if (interceptors != null) {
            for (Interceptor interceptor : interceptors) {
                builder.addInterceptor(interceptor);
            }
        }

        builder.dispatcher(new Dispatcher(executorService));

        if (configuration != null) {
            configuration.configOkhttp(application, builder);
        }
        return builder.build();
    }

    @Singleton
    @Provides
    static Retrofit.Builder provideRetrofitBuilder() {
        return new Retrofit.Builder();
    }

    @Singleton
    @Provides
    static OkHttpClient.Builder provideClientBuilder() {
        return new OkHttpClient.Builder();
    }

    @Singleton
    @Provides
    static RxErrorHandler proRxErrorHandler(Application application, ResponseErrorListener listener) {
        return RxErrorHandler
                .builder()
                .with(application)
                .responseErrorListener(listener)
                .build();
    }

    @Binds
    abstract Interceptor bindInterceptor(RequestInterceptor interceptor);

    public interface RetrofitConfiguration {
        void configRetrofit(@NonNull Context context, @NonNull Retrofit.Builder builder);
    }

    public interface OkhttpConfiguration {
        void configOkhttp(@NonNull Context context, @NonNull OkHttpClient.Builder builder);
    }

}
