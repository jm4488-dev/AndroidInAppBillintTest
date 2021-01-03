package com.jm4488.billingtest.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.android.billingclient.api.*
import com.jm4488.billingtest.R
import com.jm4488.billingtest.adapter.BillingPurchaseAdapter
import com.jm4488.billingtest.data.BillingProductItem
import com.jm4488.billingtest.databinding.ActivityBillingPurchaseBinding
import com.jm4488.billingtest.utils.GoogleBillingUtils

class BillingPurchaseActivity : AppCompatActivity() {
    private lateinit var billingClient: BillingClient

    private lateinit var binding: ActivityBillingPurchaseBinding
    private lateinit var purchaseAdapter: BillingPurchaseAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_billing_purchase)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_billing_purchase)

        init()
        setupBillingClient()
    }

    private fun init() {
        purchaseAdapter = BillingPurchaseAdapter(this)
        binding.rvList.layoutManager = LinearLayoutManager(this)
        binding.rvList.itemAnimator?.let {
            when (it) {
                is SimpleItemAnimator -> it.supportsChangeAnimations = false
            }
        }
        binding.rvList.adapter = purchaseAdapter

        binding.btnLoadPurchases.setOnClickListener {
            if (billingClient.isReady) {
                loadPurchaseItems()
            }
        }
    }

    private fun setupBillingClient() {
        billingClient = GoogleBillingUtils.getInstance(this, perchaseListener)
        purchaseAdapter.settingBillingClient(billingClient)

        billingClient.startConnection(object: BillingClientStateListener {
            override fun onBillingSetupFinished(p0: BillingResult) {
                Log.e("[TEST]", "=== onBillingSetupFinished ===")
                if (p0.responseCode == BillingClient.BillingResponseCode.OK) {
                    Log.e("[TEST]", "success to connect billing OK")

                    loadPurchaseItems()

                    val purchases = billingClient.queryPurchases(BillingClient.SkuType.INAPP).purchasesList
                    if ((purchases?.size ?: 0) > 0) {
                        // already has subs
                        Log.e("[TEST]", "Already has subscriptions : ${purchases?.size ?: 0}")
                    } else {
                        // no subs
//                        loadPurchaseItems()
                    }
                } else {
                    Log.e("[TEST]", "() Error code : ${p0.responseCode}")
                }
            }

            override fun onBillingServiceDisconnected() {
                Log.e("[TEST]", "=== onBillingServiceDisconnected ===")
                Log.e("[TEST]", "Disconnected From Billing Service")
            }

        })
    }

    private fun loadPurchaseItems() {
        Log.e("[TEST]", "=== loadSubscriptionItems ===")
        binding.pbLoading.visibility = View.VISIBLE
        if (billingClient.isReady) {
            val params = SkuDetailsParams.newBuilder()
                .setSkusList(listOf("test_001"))
                .setType(BillingClient.SkuType.INAPP)
                .build()

            billingClient.querySkuDetailsAsync(params) { response, list ->
                if (response.responseCode == BillingClient.BillingResponseCode.OK) {
                    list?.let {
                        Log.e("[TEST]", "available purchase item count : ${list.size}")
                        makeList(list)
                    }
                } else {
                    Log.e("[TEST]", "querySkuDetailsAsync error : ${response.responseCode}")
                }
                binding.pbLoading.visibility = View.GONE
            }
        }
    }

    private fun makeList(list: MutableList<SkuDetails>) {
        Log.e("[TEST]", "=== makeList ===")
        val productList = arrayListOf<BillingProductItem>()
        for (item in list) {
            Log.e("[TEST]", "subs Item : ${item.toString()}")
            productList.add(BillingProductItem(item))
        }
        purchaseAdapter.items = productList
        purchaseAdapter.notifyDataSetChanged()
    }

    private val billingListener = ConsumeResponseListener { billingResult, s ->
        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
            Log.e("[TEST]", "=== ConsumeResponseListener ===")
            Log.e("[TEST]", "Consume OK")
        }
    }

    private val perchaseListener = PurchasesUpdatedListener { billingResult, mutableList ->
        Log.e("[TEST]", "=== onPurchasesUpdated ===")
        Log.e("[TEST]", "")
    }
}