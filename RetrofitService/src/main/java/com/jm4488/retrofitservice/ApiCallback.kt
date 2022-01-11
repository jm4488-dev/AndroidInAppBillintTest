package com.jm4488.retrofitservice

import android.text.TextUtils
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.HttpURLConnection

abstract class ApiCallback<T> : Callback<T> {

    override fun onResponse(call: Call<T>, response: Response<T>?) {
        response?.let { response ->
            if (response.isSuccessful) {
                if (response.body() == null) {
                    onFailed(Throwable("Code " + response.code() + " : empty data"))
                } else {
                    onSuccess(response.body())
                }
            } else if (response.raw().networkResponse() != null
                && response.raw().networkResponse()!!.code() == HttpURLConnection.HTTP_NOT_MODIFIED) {
                onNotModified()
            } else {
                response.errorBody()?.let {
                    try {
                        JSONObject(it.string()).apply {
                            try {
                                val resultCode = getString("resultcode")
                                val resultMessage = getString("resultmessage")

                                var code = -1
                                if (!TextUtils.isEmpty(resultCode)) {
                                    try {
                                        code = resultCode.toInt()
                                    } catch (e1 : Exception) {

                                    }
                                }
                                onFailed(code, resultMessage)
                            } catch (e: JSONException) {
                                onFailed(response.code(), e.localizedMessage)
                            }
                        }
                    } catch (e: Exception) {
                        onFailed(response.code(), e.localizedMessage)
                    }
                }
            }
        }
    }

    override fun onFailure(call: Call<T>, t: Throwable) {
        if (call.isCanceled) {
            onFailed(null)
        } else {
            onFailed(t)
        }
    }

    abstract fun onSuccess(response: T?)
    abstract fun onNotModified()
    abstract fun onFailed(throwable: Throwable?)
    abstract fun onFailed(code: Int, message: String)
}