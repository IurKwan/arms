package com.fjx.arms.mvp;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.OnLifecycleEvent;

import com.fjx.arms.integration.IRepositoryManager;

/**
 * @author guanzhirui
 * @date 2022/8/17 17:52
 */
public class ArmModel implements IModel, LifecycleObserver {

    protected IRepositoryManager mRepositoryManager;

    public ArmModel(IRepositoryManager repositoryManager){
        mRepositoryManager = repositoryManager;
    }

    @Override
    public void onDestroy() {
        mRepositoryManager = null;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    void onDestroy(LifecycleOwner owner) {
        owner.getLifecycle().removeObserver(this);
    }

}
