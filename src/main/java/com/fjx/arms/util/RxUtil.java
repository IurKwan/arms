package com.fjx.arms.util;

import com.fjx.arms.mvp.IView;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.ObservableTransformer;
import io.reactivex.rxjava3.schedulers.Schedulers;

/**
 * Created by JessYan on 30/03/2018 17:16
 * @author Administrator
 */
public class RxUtil {

    private RxUtil() {
        throw new IllegalStateException("you can't instantiate me!");
    }

    /**
     * 执行io线程, 取消注册在io, 回掉主线程
     */
    public static <T> ObservableTransformer<T, T> io_main() {
        return upstream -> upstream
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 都在io线程
     */
    public static <T> ObservableTransformer<T, T> all_io() {
        return upstream -> upstream
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io());
    }

    public static <T> ObservableTransformer<T, T> applySchedulers(final IView view) {
        return observable -> observable.subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> {
                    view.showLoading("");
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(view::hideLoading).compose(RxLifecycleUtils.bindToLifecycle(view));
    }

    public static <T> ObservableTransformer<T, T> applySchedulers(final IView view, boolean pullToRequest) {
        return observable -> observable.subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> {
                    if (pullToRequest) {
                        view.showLoading("");
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> {
                    if (pullToRequest) {
                        view.hideLoading();
                    }
                }).compose(RxLifecycleUtils.bindToLifecycle(view));
    }

}
