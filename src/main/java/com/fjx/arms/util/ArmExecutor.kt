package com.fjx.arms.util

import android.annotation.SuppressLint
import okhttp3.internal.threadFactory
import java.util.concurrent.BlockingQueue
import java.util.concurrent.SynchronousQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicLong
import java.util.concurrent.locks.Condition
import java.util.concurrent.locks.ReentrantLock

/**
 * @author guanzhirui
 * @date 2022/7/29 17:59
 */
object ArmExecutor {

    private var isPaused: Boolean = false
    private var armExecutor: ThreadPoolExecutor
    private var lock: ReentrantLock = ReentrantLock()
    private var pauseCondition: Condition = lock.newCondition()

    init {
        val cpuCount = Runtime.getRuntime().availableProcessors()
        val corePoolSize = cpuCount + 1
        val maxPoolSize = cpuCount * 2 + 1
        val blockingQueue: SynchronousQueue<Runnable> = SynchronousQueue()
        val keepAliveTime = 60L
        val unit = TimeUnit.SECONDS

        val seq = AtomicLong()
        val threadFactory = threadFactory("Arms Executor" + seq.getAndIncrement(),false)

        armExecutor = object : ThreadPoolExecutor(
            corePoolSize,
            maxPoolSize,
            keepAliveTime,
            unit,
            blockingQueue as BlockingQueue<Runnable>,
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

    fun execute(runnable: Runnable) {
        armExecutor.execute(runnable)
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

    fun instance(): ThreadPoolExecutor {
        return armExecutor
    }
}