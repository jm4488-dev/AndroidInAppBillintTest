package com.jm4488.billingtest

import android.app.Application
import android.util.Log
import com.jm4488.billingtest.utils.GoogleBillingUtilsDev

class GlobalApplication : Application() {

    val googleBillingUtilsDev: GoogleBillingUtilsDev
        get() = GoogleBillingUtilsDev.getInstance(this)

    override fun onCreate() {
        super.onCreate()
        Log.e("[TEST]", "GlobalApplication onCreate")
    }
}