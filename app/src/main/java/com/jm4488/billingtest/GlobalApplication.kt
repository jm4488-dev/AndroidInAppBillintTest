package com.jm4488.billingtest

import android.app.Application
import android.util.Log
import com.jm4488.billingtest.network.NetworkParam
import com.jm4488.billingtest.utils.GoogleBillingUtils
import com.jm4488.retrofitservice.RestfulService

class GlobalApplication : Application() {

    companion object {
        lateinit var globalApplication: GlobalApplication
    }

    val googleBillingUtils: GoogleBillingUtils
        get() = GoogleBillingUtils.getInstance(this)

    override fun onCreate() {
        super.onCreate()
        Log.e("[TEST]", "GlobalApplication onCreate")
        globalApplication = this
        googleBillingUtils.initBillintClient()
        RestfulService.getInstance().initRetrofitBuilder(NetworkParam.baseUrl)
    }
}