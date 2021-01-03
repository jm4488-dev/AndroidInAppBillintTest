package com.jm4488.billingtest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.android.billingclient.api.*
import com.jm4488.billingtest.activity.BillingPurchaseActivity
import com.jm4488.billingtest.activity.BillingSubscribeActivity
import com.jm4488.billingtest.adapter.PurchasedItemAdapter
import com.jm4488.billingtest.billing.BillingViewModel
import com.jm4488.billingtest.utils.GoogleBillingUtils
import com.jm4488.billingtest.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var billingUtils: GoogleBillingUtils
    private lateinit var billingViewModel: BillingViewModel

    private lateinit var binding: ActivityMainBinding
    private lateinit var purchasedAdapter: PurchasedItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()
        billingViewModel = ViewModelProviders.of(this).get(BillingViewModel::class.java)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        billingUtils = (application as GlobalApplication).googleBillingUtils
//        lifecycle.addObserver(billingUtils)

        init()

        billingUtils.alreadyPurchasedLiveData.observe(this, Observer { purchases ->
            purchases?.let { list ->
                makeAlreadyPurchasedList(purchases)
            }
        })
        billingUtils.queryAlreadyPurchases()
    }

    private fun init() {
        binding.vm = billingViewModel

        purchasedAdapter = PurchasedItemAdapter()

        binding.rvProductList.layoutManager = LinearLayoutManager(this)
        binding.rvProductList.itemAnimator?.let {
            when (it) {
                is SimpleItemAnimator -> it.supportsChangeAnimations = false
            }
        }
        binding.rvProductList.adapter = purchasedAdapter

        binding.btnAlreadyPurchasedList.setOnClickListener {
            Log.e("[MAINACT]", "=== btn_load_product Click ===")
            billingUtils.queryAlreadyPurchases()
        }

        binding.btnGoPurchaseProduct.setOnClickListener {
            startActivity(Intent(baseContext, BillingPurchaseActivity::class.java))
        }

        binding.btnGoSubscribeProduct.setOnClickListener {
            startActivity(Intent(baseContext, BillingSubscribeActivity::class.java))
        }
    }

    private fun makeAlreadyPurchasedList(purchasedItems: List<Purchase>) {
        Log.e("[MAINACT]", "=== makeAlreadyPurchasedList ===")
        binding.pbLoading.visibility = View.VISIBLE
        purchasedAdapter.items.clear()
        purchasedAdapter.items = ArrayList(purchasedItems)
        binding.pbLoading.visibility = View.GONE
        purchasedAdapter.notifyDataSetChanged()
    }

//    private fun handleAlreadyPurchasedItem(item: Purchase) {
//        if (item.purchaseState == Purchase.PurchaseState.PURCHASED) {
//            if (!item.isAcknowledged) {
//                val params = AcknowledgePurchaseParams.newBuilder()
//                    .setPurchaseToken(item.purchaseToken)
//                    .build()
//
//                billingUtils..acknowledgePurchase(params, acknowledgePurchaseResponseListener)
//            } else {
//
//            }
//        }
//    }

    private val acknowledgePurchaseResponseListener = AcknowledgePurchaseResponseListener { result ->
        Log.e("[MAINACT]", "=== acknowledgePurchaseResponseListener ===")
        if (result.responseCode == BillingClient.BillingResponseCode.OK) {
            Log.e("[MAINACT]", "RESPONSE OK")
        }
    }
}