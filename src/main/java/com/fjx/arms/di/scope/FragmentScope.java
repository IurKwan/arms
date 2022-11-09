package com.fjx.arms.di.scope;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;

import javax.inject.Scope;

/**
 * @author guanzhirui
 * @date 2022/11/9 9:45
 */
@Scope
@Documented
@Retention(RUNTIME)
public @interface FragmentScope {}
