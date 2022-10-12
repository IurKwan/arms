package com.fjx.arms.retrofit;

import android.text.TextUtils;

import com.blankj.utilcode.util.Utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * @author IurKwan
 * @date 2022/7/24
 */
public class RetrofitManager {

    private boolean isRun = true;

    private final Interceptor mInterceptor;

    private static final String DOMAIN_NAME = "Domain-Name";

    public static final String IDENTIFICATION_IGNORE = "#url_ignore";

    private static final String GLOBAL_DOMAIN_NAME = "com.fjx.retrofitmanager.globalDomainName";

    public static final String DOMAIN_NAME_HEADER = DOMAIN_NAME + ": ";

    private final Map<String, HttpUrl> mDomainNameHub = new HashMap<>();

    private final DomainUrlParser mUrlParser;

    private RetrofitManager() {
        mUrlParser = new DomainUrlParser();
        mInterceptor = chain -> {
            if (!isRun()) {
                return chain.proceed(chain.request());
            }
            return chain.proceed(processRequest(chain.request()));
        };
    }

    private static class RetrofitManagerHolder {
        private static final RetrofitManager INSTANCE = new RetrofitManager();
    }

    public static RetrofitManager getInstance() {
        return RetrofitManagerHolder.INSTANCE;
    }

    public OkHttpClient.Builder with(OkHttpClient.Builder builder) {
        return builder
                .addInterceptor(mInterceptor);
    }

    public Request processRequest(Request request) {
        if (request == null) {
            return null;
        }
        Request.Builder builder = request.newBuilder();
        String url = request.url().toString();
        if (url.contains(IDENTIFICATION_IGNORE)) {
            return pruneIdentification(builder, url);
        }

        String domainName = obtainDomainNameFormHeaders(request);

        HttpUrl httpUrl;

        if (!TextUtils.isEmpty(domainName)) {
            httpUrl = fetchDomain(domainName);
            builder.removeHeader(DOMAIN_NAME);
        } else {
            httpUrl = getGlobalDomain();
        }

        if (httpUrl != null) {
            HttpUrl newUrl = mUrlParser.parseUrl(httpUrl, request.url());
            return builder
                    .url(newUrl)
                    .build();
        }

        return builder.build();
    }

    private Request pruneIdentification(Request.Builder builder, String url) {
        String[] split = url.split(IDENTIFICATION_IGNORE);
        StringBuilder buffer = new StringBuilder();
        for (String s : split) {
            buffer.append(s);
        }
        return builder.url(buffer.toString())
                .build();
    }

    private String obtainDomainNameFormHeaders(Request request) {
        List<String> headers = request.headers(DOMAIN_NAME);
        if (headers.size() == 0) {
            return null;
        }
        if (headers.size() > 1) {
            throw new IllegalArgumentException("Only one Domain-Name in the headers");
        }
        return request.header(DOMAIN_NAME);
    }

    public synchronized HttpUrl fetchDomain(String domainName) {
        return mDomainNameHub.get(domainName);
    }

    public void setGlobalDomain(String globalDomain) {
        synchronized (mDomainNameHub) {
            mDomainNameHub.put(GLOBAL_DOMAIN_NAME, HttpUrl.parse(globalDomain));
        }
    }

    public synchronized HttpUrl getGlobalDomain() {
        return mDomainNameHub.get(GLOBAL_DOMAIN_NAME);
    }

    public void removeGlobalDomain() {
        synchronized (mDomainNameHub) {
            mDomainNameHub.remove(GLOBAL_DOMAIN_NAME);
        }
    }

    public boolean isRun() {
        return this.isRun;
    }

    public void setRun(boolean run) {
        isRun = run;
    }

    public void putDomain(String domainName, String domainUrl) {
        synchronized (mDomainNameHub) {
            mDomainNameHub.put(domainName, HttpUrl.parse(domainUrl));
        }
    }

    public String setUrlNotChange(String url) {
        return url + IDENTIFICATION_IGNORE;
    }
}
