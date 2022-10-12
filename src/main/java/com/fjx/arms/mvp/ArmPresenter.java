package com.fjx.arms.mvp;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.OnLifecycleEvent;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;

/**
 * @author guanzhirui
 * @date 2022/8/17 17:17
 */
public class ArmPresenter<M extends IModel, V extends IView> implements IPresenter, LifecycleObserver {

    protected CompositeDisposable mCompositeDisposable;
    protected M mModel;
    protected V mRootView;

    public ArmPresenter(M mModel, V mRootView) {
        this.mModel = mModel;
        this.mRootView = mRootView;
        onStart();
    }

    public ArmPresenter() {
        onStart();
    }

    @Override
    public void onStart() {
        if (mRootView != null && mRootView instanceof LifecycleOwner) {
            ((LifecycleOwner) mRootView).getLifecycle().addObserver(this);
            if (mModel != null && mModel instanceof LifecycleObserver) {
                ((LifecycleOwner) mRootView).getLifecycle().addObserver((LifecycleObserver) mModel);
            }
        }
    }

    @Override
    public void onDestroy() {
        unDispose();
        if (mModel != null) {
            mModel.onDestroy();
        }
        this.mModel = null;
        this.mRootView = null;
        this.mCompositeDisposable = null;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    void onDestroy(LifecycleOwner owner) {
        owner.getLifecycle().removeObserver(this);
    }

    public void addDispose(Disposable disposable) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable();
        }
        mCompositeDisposable.add(disposable);
    }

    public void unDispose() {
        if (mCompositeDisposable != null) {
            mCompositeDisposable.clear();
        }
    }

}
