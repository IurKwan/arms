/**
 * Copyright 2017 JessYan
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.fjx.arms.rxerror.handler;

import com.fjx.arms.rxerror.core.RxErrorHandler;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.annotations.Nullable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import retrofit2.HttpException;

/**
 * @author Administrator
 */
public abstract class ErrorHandleSubscriber<T> implements Observer<T> {

    private final ErrorHandlerFactory mHandlerFactory;

    public ErrorHandleSubscriber(RxErrorHandler rxErrorHandler) {
        this.mHandlerFactory = rxErrorHandler.getHandlerFactory();
    }

    @Override
    public void onSubscribe(@NonNull Disposable d) {

    }


    @Override
    public void onComplete() {

    }


    @Override
    public void onError(@NonNull Throwable t) {
        t.printStackTrace();
        if (t instanceof HttpException) {
            onHttpError((HttpException) t);
        } else {
            mHandlerFactory.handleError(t);
        }
    }

    public void onHttpError(@NonNull HttpException t) {
        mHandlerFactory.handleError(t);
    }

}

