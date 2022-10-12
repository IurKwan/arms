package com.fjx.arms.integration.lifecycle;

import androidx.annotation.NonNull;

import io.reactivex.rxjava3.subjects.Subject;


/**
 * @author guanzhirui
 * @date 2022/8/1 10:58
 */
public interface LifecycleAble<E> {

    @NonNull
    Subject<E> provideLifecycleSubject();

}
