package com.jm4488.billingtest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.android.billingclient.api.*
import com.jm4488.billingtest.activity.BillingPurchaseActivity
import com.jm4488.billingtest.activity.BillingSubscribeActivity
import com.jm4488.billingtest.adapter.PurchasedItemAdapter
import com.jm4488.billingtest.billing.BillingViewModel
import com.jm4488.billingtest.data.PurchasedItem
import com.jm4488.billingtest.utils.GoogleBillingUtils
import com.jm4488.billingtest.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var billingClient: BillingClient
    private lateinit var billingViewModel: BillingViewModel

    private lateinit var binding: ActivityMainBinding
    private lateinit var purchasedAdapter: PurchasedItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        binding = DataBindingUtil.inflate(inflater, getLayoutRes(), container, false)

        billingViewModel = ViewModelProviders.of(this).get(BillingViewModel::class.java)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        setupBillingClient()
        init()
    }

    private fun init() {
        binding.vm = billingViewModel

        purchasedAdapter = PurchasedItemAdapter(this)
        purchasedAdapter.settingBillingClient(billingClient)

        binding.rvProductList.layoutManager = LinearLayoutManager(this)
        binding.rvProductList.itemAnimator?.let {
            when (it) {
                is SimpleItemAnimator -> it.supportsChangeAnimations = false
            }
        }
        binding.rvProductList.adapter = purchasedAdapter

        binding.btnAlreadyPurchasedList.setOnClickListener {
            Log.e("[TEST]", "=== btn_load_product Click ===")
            if (billingClient.isReady) {
                Log.e("[TEST]", "client ready : ${billingClient.isReady}")
                makeAlreadyPurchasedList()
            }
        }

        binding.btnGoPurchaseProduct.setOnClickListener {
            startActivity(Intent(baseContext, BillingPurchaseActivity::class.java))
        }

        binding.btnGoSubscribeProduct.setOnClickListener {
            startActivity(Intent(baseContext, BillingSubscribeActivity::class.java))
        }
    }

    private fun setupBillingClient() {
        billingClient = GoogleBillingUtils.getInstance(this, purchaseListener)
        billingClient.startConnection(object: BillingClientStateListener {
            override fun onBillingSetupFinished(p0: BillingResult) {
                Log.e("[TEST]", "=== onBillingSetupFinished ===")
                if (p0.responseCode == BillingClient.BillingResponseCode.OK) {
                    Log.e("[TEST]", "success to connect billing OK")
                    makeAlreadyPurchasedList()
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

    private fun makeAlreadyPurchasedList() {
        Log.e("[TEST]", "=== makeAlreadyPurchasedList ===")
        binding.pbLoading.visibility = View.VISIBLE
        purchasedAdapter.items.clear()

        billingClient.queryPurchases(BillingClient.SkuType.INAPP).purchasesList?.let {
            Log.e("[TEST]", "INAPP items size : ${it.size}")
            for (item in it) {
                Log.e("[TEST]", "INAPP item : ${item.toString()}")
                purchasedAdapter.items.add(PurchasedItem(item))
            }
        }

        billingClient.queryPurchases(BillingClient.SkuType.SUBS).purchasesList?.let {
            Log.e("[TEST]", "SUBS items size : ${it.size}")
            for (item in it) {
                Log.e("[TEST]", "SUBS item : ${item.toString()}")

                purchasedAdapter.items.add(PurchasedItem(item))
            }
        }
        binding.pbLoading.visibility = View.GONE
        purchasedAdapter.notifyDataSetChanged()
    }

    private fun handleAlreadyPurchasedItem(item: Purchase) {
        if (item.purchaseState == Purchase.PurchaseState.PURCHASED) {
            if (!item.isAcknowledged) {
                val params = AcknowledgePurchaseParams.newBuilder()
                    .setPurchaseToken(item.purchaseToken)
                    .build()

                billingClient.acknowledgePurchase(params, acknowledgePurchaseResponseListener)
            } else {

            }
        }
    }

    private val acknowledgePurchaseResponseListener = AcknowledgePurchaseResponseListener { result ->
        Log.e("[TEST]", "=== acknowledgePurchaseResponseListener ===")
        if (result.responseCode == BillingClient.BillingResponseCode.OK) {
            Log.e("[TEST]", "RESPONSE OK")
        }
    }

    private val purchaseListener = PurchasesUpdatedListener { billingResult, mutableList ->
        Log.e("[TEST]", "=== onPurchasesUpdated ===")
        if (billingClient.isReady && billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
            mutableList?.let {
                for (item in it) {
                    purchasedAdapter.items.add(PurchasedItem(item))
                }
            }
        } else {
            Log.e("[TEST]", "PurchasesUpdatedListener Error code : ${billingResult.responseCode}")
        }
    }

}