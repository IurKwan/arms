package com.fjx.arms.retrofit;

import android.text.TextUtils;

import com.blankj.utilcode.util.CacheMemoryStaticUtils;

import java.util.ArrayList;
import java.util.List;

import okhttp3.HttpUrl;

/**
 * @author IurKwan
 * @date 2022/7/24
 */
public class DomainUrlParser {

    public HttpUrl parseUrl(HttpUrl domainUrl, HttpUrl url) {
        if (domainUrl == null){
            return url;
        }

        HttpUrl.Builder builder = url.newBuilder();

        String key = getKey(domainUrl, url);
        if (TextUtils.isEmpty(CacheMemoryStaticUtils.get(key))){
            for (int i = 0;i < url.pathSize();i++){
                builder.removePathSegment(0);
            }
            List<String> newPathSegments = new ArrayList<>();
            newPathSegments.addAll(domainUrl.encodedPathSegments());
            newPathSegments.addAll(url.encodedPathSegments());

            for (String pathSegment : newPathSegments){
                builder.addEncodedPathSegment(pathSegment);
            }
        } else {
            builder.encodedPath(CacheMemoryStaticUtils.get(key));
        }
        HttpUrl httpUrl = builder
                .scheme(domainUrl.scheme())
                .host(domainUrl.host())
                .port(domainUrl.port())
                .build();
        if (TextUtils.isEmpty(CacheMemoryStaticUtils.get(key))){
            CacheMemoryStaticUtils.put(key,httpUrl.encodedPath());
        }
        return httpUrl;
    }

    private String getKey(HttpUrl domainUrl,HttpUrl url){
        return domainUrl.encodedPath() + url.encodedPath();
    }
}
