package com.fjx.arms.util

import android.annotation.SuppressLint
import okhttp3.internal.threadFactory
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.ScheduledThreadPoolExecutor
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicLong
import java.util.concurrent.locks.Condition
import java.util.concurrent.locks.ReentrantLock

/**
 * 基于ScheduledExecutorService定时器
 * 可以添加多个定时任务
 * @author guanzhirui
 * @date 2022/8/16 16:44
 */
object ArmTimingExecutor {

    private var isPaused: Boolean = false
    private var timingService: ScheduledExecutorService? = null
    private var lock: ReentrantLock = ReentrantLock()
    private var pauseCondition: Condition = lock.newCondition()

    init {
        val cpuCount = Runtime.getRuntime().availableProcessors()
        val seq = AtomicLong()
        val threadFactory = threadFactory("Arms Timing Executor" + seq.getAndIncrement(), false)

        timingService = object : ScheduledThreadPoolExecutor(
            cpuCount,
            threadFactory
        ) {
            override fun beforeExecute(t: Thread?, r: Runnable?) {
                if (isPaused) {
                    lock.lock()
                    try {
                        pauseCondition.await()
                    } finally {
                        lock.unlock()
                    }
                }
            }

            @SuppressLint("BinaryOperationInTimber")
            override fun afterExecute(r: Runnable?, t: Throwable?) {
                //监控线程池耗时任务,线程创建数量,正在运行的数量
            }
        }
    }

    fun schedule(delay: Long, runnable: Runnable) {
        schedule(delay, TimeUnit.MILLISECONDS, runnable)
    }

    @JvmStatic
    fun schedule(delay: Long, unit: TimeUnit, runnable: Runnable) {
        timingService?.schedule(runnable, delay, unit)
    }

    fun scheduleAtFixedRate(period: Long, runnable: Runnable) {
        scheduleAtFixedRate(0, period, TimeUnit.MILLISECONDS, runnable)
    }

    @JvmStatic
    fun scheduleAtFixedRate(initialDelay: Long, period: Long, runnable: Runnable) {
        scheduleAtFixedRate(initialDelay, period, TimeUnit.MILLISECONDS, runnable)
    }

    fun scheduleAtFixedRate(
        initialDelay: Long,
        period: Long,
        unit: TimeUnit,
        runnable: Runnable
    ) {
        timingService?.scheduleAtFixedRate(
            runnable,
            initialDelay,
            period,
            unit
        )
    }

    fun pause() {
        lock.lock()
        try {
            isPaused = true
        } finally {
            lock.unlock()
        }
    }

    fun resume() {
        lock.lock()
        try {
            isPaused = false
            pauseCondition.signalAll()
        } finally {
            lock.unlock()
        }
    }


}