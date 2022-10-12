package com.fjx.arms.http.factory

import android.annotation.SuppressLint
import android.util.Log
import okhttp3.Call
import okhttp3.EventListener
import java.io.IOException
import java.net.InetAddress

/**
 * @author guanzhirui
 * @date 2022/8/1 14:47
 */
open class OkhttpEventListenerFactory : EventListener() {

    companion object {

        private const val TAG = "OkhttpEventListener"

        @JvmField
        val FACTORY = Factory { OkhttpEventListenerFactory() }

    }

    /**
     * 统计对象
     */
    private val okHttpEvent = OkHttpEvent()

    /**
     * DNS解析开始
     */
    override fun dnsStart(call: Call, domainName: String) {
        super.dnsStart(call, domainName)
        okHttpEvent.dnsStartTime = System.currentTimeMillis()
    }

    /**
     * DNS解析结束
     */
    override fun dnsEnd(call: Call, domainName: String, inetAddressList: List<InetAddress>) {
        super.dnsEnd(call, domainName, inetAddressList)
        okHttpEvent.dnsEndTime = System.currentTimeMillis()
    }

    /**
     * 响应体处理结束
     */
    override fun responseBodyEnd(call: Call, byteCount: Long) {
        super.responseBodyEnd(call, byteCount)
        okHttpEvent.responseBodySize = byteCount
    }

    /**
     * 请求完成
     */
    override fun callEnd(call: Call) {
        super.callEnd(call)
        okHttpEvent.success = true
    }

    /**
     * 请求失败
     */
    @SuppressLint("LogNotTimber")
    override fun callFailed(call: Call, ioe: IOException) {
        super.callFailed(call, ioe)
        okHttpEvent.success = false
        okHttpEvent.errorStack = Log.getStackTraceString(ioe)
        Log.e(TAG, "HTTP error stack : ${okHttpEvent.errorStack}")
    }

}