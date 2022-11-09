package com.fjx.arms.base;

import androidx.annotation.ContentView;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.blankj.utilcode.util.CacheMemoryUtils;
import com.fjx.arms.base.delegate.IActivity;
import com.fjx.arms.di.component.AppComponent;
import com.fjx.arms.integration.cache.Cache;
import com.fjx.arms.integration.cache.CacheType;
import com.fjx.arms.integration.lifecycle.ActivityLifecycleAble;
import com.fjx.arms.mvp.IPresenter;
import com.fjx.arms.util.ArmUtil;
import com.trello.rxlifecycle4.android.ActivityEvent;

import javax.inject.Inject;

import io.reactivex.rxjava3.subjects.BehaviorSubject;
import io.reactivex.rxjava3.subjects.Subject;

/**
 * @author IurKwan
 * @date 2022/8/11
 */
public abstract class ArmActivity<P extends IPresenter> extends AppCompatActivity implements IActivity, ActivityLifecycleAble {

    private final BehaviorSubject<ActivityEvent> mLifecycleSubject = BehaviorSubject.create();

    private Cache<String, Object> mCache;

    @Inject
    protected P mPresenter;

    public ArmActivity() {
        super();
    }

    @ContentView
    public ArmActivity(@LayoutRes int contentLayoutId) {
        super(contentLayoutId);
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {

    }

    @NonNull
    @Override
    public synchronized Cache<String, Object> provideCache() {
        if (mCache == null) {
            //noinspection unchecked
            mCache = ArmUtil.obtainAppComponent(this).cacheFactory().build(CacheType.ACTIVITY_CACHE);
        }
        return mCache;
    }

    @NonNull
    @Override
    public Subject<ActivityEvent> provideLifecycleSubject() {
        return mLifecycleSubject;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null){
            mPresenter.onDestroy();
        }
        this.mPresenter = null;
    }

}
