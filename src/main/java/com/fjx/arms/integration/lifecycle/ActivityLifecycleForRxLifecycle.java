package com.fjx.arms.integration.lifecycle;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.trello.rxlifecycle4.android.ActivityEvent;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.rxjava3.subjects.Subject;

/**
 * @author guanzhirui
 * @date 2022/8/1 10:55
 */
@Singleton
public class ActivityLifecycleForRxLifecycle implements Application.ActivityLifecycleCallbacks {

    @Inject
    public ActivityLifecycleForRxLifecycle() {

    }

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
        if (activity instanceof ActivityLifecycleAble) {
            obtainSubject(activity).onNext(ActivityEvent.CREATE);
        }
    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {
        if (activity instanceof ActivityLifecycleAble) {
            obtainSubject(activity).onNext(ActivityEvent.START);
        }
    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {
        if (activity instanceof ActivityLifecycleAble) {
            obtainSubject(activity).onNext(ActivityEvent.RESUME);
        }
    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {
        if (activity instanceof ActivityLifecycleAble) {
            obtainSubject(activity).onNext(ActivityEvent.PAUSE);
        }
    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {
        if (activity instanceof ActivityLifecycleAble) {
            obtainSubject(activity).onNext(ActivityEvent.STOP);
        }
    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {
        if (activity instanceof ActivityLifecycleAble) {
            obtainSubject(activity).onNext(ActivityEvent.DESTROY);
        }
    }

    private Subject<ActivityEvent> obtainSubject(Activity activity){
        return ((ActivityLifecycleAble)activity).provideLifecycleSubject();
    }

}
