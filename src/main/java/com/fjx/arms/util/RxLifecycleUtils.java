package com.fjx.arms.util;

import androidx.annotation.NonNull;

import com.fjx.arms.integration.lifecycle.ActivityLifecycleAble;
import com.fjx.arms.integration.lifecycle.LifecycleAble;
import com.fjx.arms.mvp.IView;
import com.trello.rxlifecycle4.LifecycleTransformer;
import com.trello.rxlifecycle4.RxLifecycle;
import com.trello.rxlifecycle4.android.ActivityEvent;
import com.trello.rxlifecycle4.android.RxLifecycleAndroid;

/**
 * @author guanzhirui
 * @date 2022/8/18 15:39
 */
public class RxLifecycleUtils {

    private RxLifecycleUtils() {
        throw new IllegalStateException("you can't instantiate me!");
    }

    /**
     * 绑定 Activity 的指定生命周期
     *
     * @param view
     * @param event
     * @param <T>
     * @return
     */
    public static <T> LifecycleTransformer<T> bindUntilEvent(@NonNull final IView view,
                                                             final ActivityEvent event) {
        if (view instanceof ActivityLifecycleAble) {
            return bindUntilEvent((ActivityLifecycleAble) view, event);
        } else {
            throw new IllegalArgumentException("view isn't ActivityLifecycleable");
        }
    }

    public static <T, R> LifecycleTransformer<T> bindUntilEvent(@NonNull final LifecycleAble<R> lifecycleable,
                                                                final R event) {
        return RxLifecycle.bindUntilEvent(lifecycleable.provideLifecycleSubject(), event);
    }

    /**
     * 绑定 Activity/Fragment 的生命周期
     *
     * @param view
     * @param <T>
     * @return
     */
    public static <T> LifecycleTransformer<T> bindToLifecycle(@NonNull IView view) {
        if (view instanceof LifecycleAble) {
            return bindToLifecycle((LifecycleAble) view);
        } else {
            throw new IllegalArgumentException("view isn't Lifecycleable");
        }
    }

    public static <T> LifecycleTransformer<T> bindToLifecycle(@NonNull LifecycleAble lifecycleable) {
        if (lifecycleable instanceof ActivityLifecycleAble) {
            return RxLifecycleAndroid.bindActivity(((ActivityLifecycleAble) lifecycleable).provideLifecycleSubject());
        }else {
            throw new IllegalArgumentException("Lifecycleable not match");
        }
    }

}
