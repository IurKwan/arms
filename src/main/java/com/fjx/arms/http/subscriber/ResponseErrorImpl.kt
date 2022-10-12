package com.fjx.arms.http.subscriber

import android.content.Context
import com.blankj.utilcode.util.ToastUtils
import com.fjx.arms.rxerror.handler.listener.ResponseErrorListener
import com.google.gson.JsonIOException
import com.google.gson.JsonParseException
import org.json.JSONException
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.text.ParseException

/**
 * @author guanzhirui
 * @date 2022/8/1 14:18
 */
open class ResponseErrorImpl : ResponseErrorListener {

    override fun handleResponseError(context: Context?, t: Throwable?) {

        var message = ""

        when (t) {
            is UnknownHostException -> {
                message = "无连接异常"
            }
            is SocketTimeoutException -> {
                message = "请求网络超时"
            }
            is HttpException -> {
                message = convertStatusCode(t)
            }
            is JsonParseException, is android.net.ParseException, is JSONException, is JsonIOException, is ParseException -> {
                message = "数据解析错误"
            }
            is ConnectException -> {
                message = "无法连接到服务器"
            }
        }

        if (message.isNotEmpty()) {
            ToastUtils.showLong(message)
        }
    }

    private fun convertStatusCode(httpException: HttpException): String {
        // 优先显示服务器返回的错误
        var tips: String? = null
        if (tips.isNullOrEmpty()) {
            tips = when {
                httpException.code() == 400 -> {
                    "服务器发生错误"
                }
                httpException.code() == 500 -> {
                    "服务器发生错误"
                }
                httpException.code() == 404 -> {
                    "请求地址不存在"
                }
                httpException.code() == 403 -> {
                    "请求被服务器拒绝"
                }
                httpException.code() == 307 -> {
                    "请求被重定向到其他页面"
                }
                else -> {
                    httpException.message()
                }
            }
        }
        return tips ?: ""
    }

}