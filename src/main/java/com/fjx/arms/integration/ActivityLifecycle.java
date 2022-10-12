package com.fjx.arms.integration;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.fjx.arms.base.delegate.ActivityDelegate;
import com.fjx.arms.base.delegate.ActivityDelegateImpl;
import com.fjx.arms.base.delegate.IActivity;
import com.fjx.arms.integration.cache.Cache;
import com.fjx.arms.integration.cache.IntelligentCache;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author guanzhirui
 * @date 2022/8/1 9:21
 */
@Singleton
public class ActivityLifecycle implements Application.ActivityLifecycleCallbacks {

    @Inject
    public ActivityLifecycle() {
    }

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
        if (activity instanceof IActivity) {
            ActivityDelegate activityDelegate = fetchActivityDelegate(activity);
            if (activityDelegate == null) {
                Cache<String, Object> cache = getCacheFromActivity((IActivity) activity);
                activityDelegate = new ActivityDelegateImpl(activity);
                cache.put(IntelligentCache.getKeyOfKeep(ActivityDelegate.ACTIVITY_DELEGATE), activityDelegate);
            }
            activityDelegate.onCreate(savedInstanceState);
        }
    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {
        ActivityDelegate activityDelegate = fetchActivityDelegate(activity);
        if (activityDelegate != null) {
            activityDelegate.onStart();
        }
    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {
        ActivityDelegate activityDelegate = fetchActivityDelegate(activity);
        if (activityDelegate != null) {
            activityDelegate.onResume();
        }
    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {
        ActivityDelegate activityDelegate = fetchActivityDelegate(activity);
        if (activityDelegate != null) {
            activityDelegate.onPause();
        }
    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {
        ActivityDelegate activityDelegate = fetchActivityDelegate(activity);
        if (activityDelegate != null) {
            activityDelegate.onStop();
        }
    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {
        ActivityDelegate activityDelegate = fetchActivityDelegate(activity);
        if (activityDelegate != null) {
            activityDelegate.onSaveInstanceState(outState);
        }
    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {
        ActivityDelegate activityDelegate = fetchActivityDelegate(activity);
        if (activityDelegate != null) {
            activityDelegate.onDestroy();
            ((IActivity)activity).provideCache().clear();
        }
    }

    private ActivityDelegate fetchActivityDelegate(Activity activity) {
        ActivityDelegate activityDelegate = null;
        if (activity instanceof IActivity) {
            Cache<String, Object> cache = getCacheFromActivity((IActivity) activity);
            activityDelegate = (ActivityDelegate) cache.get(IntelligentCache.getKeyOfKeep(ActivityDelegate.ACTIVITY_DELEGATE));
        }
        return activityDelegate;
    }

    @NonNull
    private Cache<String, Object> getCacheFromActivity(IActivity activity) {
        return activity.provideCache();
    }

}
