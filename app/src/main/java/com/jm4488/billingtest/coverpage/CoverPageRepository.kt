package com.jm4488.billingtest.coverpage

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jm4488.billingtest.billing.InAppBillingModel
import com.jm4488.billingtest.network.NetworkParam
import com.jm4488.billingtest.network.WavveServer
import com.jm4488.retrofitservice.RestfulService
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class CoverPageRepository {
    private val COVER_ASSET_KR = "coverpage.json"

    fun getDefaultJsonText(context: Context): CoverPageModel {
        val fileName = COVER_ASSET_KR
        val jsonString = context.assets.open(fileName).bufferedReader().use {
            it.readText()
        }

        val jsonObj = JSONObject(jsonString)
        return Gson().fromJson<CoverPageModel>(jsonObj.getJSONObject("cover_page").toString(), object : TypeToken<CoverPageModel>() {}.type)
    }

    fun requestCoverPageJson(context: Context, callback: ApiCallback<CoverPageModel>) {

        val paramMap = NetworkParam.Builder().build().getNetworkParamsMap()
        val wavveApi: WavveServer = RestfulService.getInstance().getApiInstance(paramMap, WavveServer::class.java)
        val timeStamp = SimpleDateFormat("yyyyMMddHHmmss", Locale.KOREA).format(Calendar.getInstance().time)

        val service: Call<ResponseCoverPage> = wavveApi.requestCoverPage("https://pooq3-static-json.wavve.com/service/marketing/coverpage.json", timeStamp)
        service.enqueue(object : Callback<ResponseCoverPage?> {
            override fun onResponse(call: Call<ResponseCoverPage?>, response: Response<ResponseCoverPage?>) {
                Log.e("[Network]", "onResponse : $response")
            }

            override fun onFailure(call: Call<ResponseCoverPage?>, t: Throwable) {
                Log.e("[Network]", "onFailure : ${t.localizedMessage}")
            }
        })
    }
}