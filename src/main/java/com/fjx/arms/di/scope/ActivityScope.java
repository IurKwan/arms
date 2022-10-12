package com.fjx.arms.di.scope;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * @author guanzhirui
 * @date 2022/7/29 16:58
 */
@Scope
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface ActivityScope {
}
