package com.fjx.arms.http.factory

/**
 * @author guanzhirui
 * @date 2022/8/1 14:48
 */
class OkHttpEvent {

    /**
     * DNS 解析开始时间
     */
    var dnsStartTime = 0L

    /**
     * DNS 解析结束时间
     */
    var dnsEndTime = 0L

    /**
     * 响应体大小
     */
    var responseBodySize = 0L

    /**
     * 请求是否成功
     */
    var success = false

    /**
     * 错误原因
     */
    var errorStack : String? = null

}