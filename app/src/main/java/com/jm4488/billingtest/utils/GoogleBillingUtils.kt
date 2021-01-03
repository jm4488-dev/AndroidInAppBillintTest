package com.jm4488.billingtest.utils

import android.content.Context
import com.android.billingclient.api.*

class GoogleBillingUtils {

    companion object {
        private const val TAG = "[GoogleBillingUtils]"

        @Volatile
        private var INSTANCE: BillingClient? = null

        @JvmStatic
        fun getInstance(context: Context, listener: PurchasesUpdatedListener): BillingClient =
                INSTANCE ?: synchronized(this) {
                    INSTANCE ?: setupBillingClient(context, listener).also { INSTANCE = it }
                }

        private fun setupBillingClient(context: Context, listener: PurchasesUpdatedListener): BillingClient {
            return BillingClient.newBuilder(context)
                    .enablePendingPurchases() // Not used for subscriptions.
                    .setListener(listener)
                    .build()
        }
    }

}