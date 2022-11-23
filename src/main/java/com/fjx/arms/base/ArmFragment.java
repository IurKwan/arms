package com.fjx.arms.base;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import androidx.annotation.ContentView;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;

import com.blankj.utilcode.util.ThreadUtils;
import com.fjx.arms.base.delegate.IFragment;
import com.fjx.arms.di.component.AppComponent;
import com.fjx.arms.integration.cache.Cache;
import com.fjx.arms.integration.cache.CacheType;
import com.fjx.arms.integration.lifecycle.FragmentLifecycleable;
import com.fjx.arms.mvp.IPresenter;
import com.fjx.arms.util.ArmUtil;
import com.trello.rxlifecycle4.android.FragmentEvent;

import javax.inject.Inject;

import io.reactivex.rxjava3.subjects.BehaviorSubject;
import io.reactivex.rxjava3.subjects.Subject;

/**
 * @author guanzhirui
 * @date 2022/11/9 9:28
 */
public abstract class ArmFragment<P extends IPresenter> extends Fragment implements IFragment, FragmentLifecycleable {

    private final BehaviorSubject<FragmentEvent> mLifecycleSubject = BehaviorSubject.create();

    private Cache<String, Object> mCache;

    private boolean isFirst = true;

    private final Handler mHandler = new Handler(Looper.myLooper());

    /**
     * 如果当前页面逻辑简单, Presenter 可以为 null
     */
    @Inject
    protected P mPresenter;

    @NonNull
    @Override
    public synchronized Cache<String, Object> provideCache() {
        if (mCache == null) {
            mCache = ArmUtil.obtainAppComponent(getActivity()).cacheFactory().build(CacheType.FRAGMENT_CACHE);
        }
        return mCache;
    }

    @NonNull
    @Override
    public final Subject<FragmentEvent> provideLifecycleSubject() {
        return mLifecycleSubject;
    }

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {

    }

    public ArmFragment() {
        super();
    }

    /**
     * 封装简化ViewBind的使用
     *
     * @param contentLayoutId layout
     */
    @ContentView
    public ArmFragment(@LayoutRes int contentLayoutId) {
        super(contentLayoutId);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isFirst = true;
    }

    @Override
    public void onResume() {
        super.onResume();
        onVisible();
    }

    private void onVisible() {
        if (getLifecycle().getCurrentState() == Lifecycle.State.STARTED && isFirst) {
            // 懒加载
            mHandler.postDelayed(() -> {
                lazyLoadData();
                isFirst = false;
            }, lazyLoadTime());
        }
    }

    public void lazyLoadData() {

    }

    public long lazyLoadTime() {
        return 300;
    }

    @Override
    public void setData(@Nullable Object data) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //释放资源
        if (mPresenter != null) {
            mPresenter.onDestroy();
        }
        this.mPresenter = null;
        mHandler.removeCallbacksAndMessages(null);
    }

}