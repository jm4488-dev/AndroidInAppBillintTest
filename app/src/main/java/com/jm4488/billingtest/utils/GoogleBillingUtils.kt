package com.jm4488.billingtest.utils

import android.app.Activity
import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.android.billingclient.api.*
import com.jm4488.billingtest.data.Constants
import java.util.*

class GoogleBillingUtils  private constructor(
        private val app: Application
) : LifecycleObserver,
        PurchasesUpdatedListener,
        BillingClientStateListener,
        SkuDetailsResponseListener {

    private lateinit var billingClient: BillingClient
    val purchaseUpdateLiveData = MutableLiveData<List<Purchase>>()
    val alreadyPurchasedLiveData = MutableLiveData<ArrayList<Purchase>>()
    val productSkuDetailsLiveData = MutableLiveData<List<SkuDetails>>()

    companion object {
        private const val TAG = "[GoogleBillingUtils]"

        @Volatile
        private var INSTANCE: GoogleBillingUtils? = null

        @JvmStatic
        fun getInstance(app: Application): GoogleBillingUtils =
                INSTANCE ?: synchronized(this) {
                    INSTANCE ?: GoogleBillingUtils(app).also { INSTANCE = it }
                }
    }

    fun initBillintClient() {
        Log.e(TAG, "UTILS INIT")
        // Create a new BillingClient in onCreate().
        // Since the BillingClient can only be used once, we need to create a new instance
        // after ending the previous connection to the Google Play Store in onDestroy().
        billingClient = BillingClient.newBuilder(app.applicationContext)
                .setListener(this)
                .enablePendingPurchases() // Not used for subscriptions.
                .build()
        if (!checkBillingClient()) {
            Log.e(TAG, "BillingClient: Start connection...")
            billingClient.startConnection(this)
        }
    }

    fun checkBillingClient(): Boolean {
        if (billingClient == null) {
            Log.e(TAG, "BillingClient is null")
        } else {
            return if (billingClient.isReady) {
                Log.e(TAG, "BillingClient is ready")
                true
            } else {
                Log.e(TAG, "BillingClient is not ready")
                false
            }
        }
        return false
    }

    override fun onBillingSetupFinished(billingResult: BillingResult) {
        val responseCode = billingResult.responseCode
        val debugMessage = billingResult.debugMessage
        Log.e(TAG, "onBillingSetupFinished: $responseCode $debugMessage")
        if (responseCode == BillingClient.BillingResponseCode.OK) {
            // The billing client is ready. You can query purchases here.
        }
    }

    override fun onBillingServiceDisconnected() {
        Log.e(TAG, "onBillingServiceDisconnected")
    }

    override fun onSkuDetailsResponse(billingResult: BillingResult, mutableList: MutableList<SkuDetails>?) {
        Log.e(TAG, "=== onSkuDetailsResponse ===")
        Log.e(TAG, "result code : ${billingResult.responseCode}")
        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
            mutableList?.let {
                Log.e(TAG, "mutableList size : ${it.size}")
                productSkuDetailsLiveData.postValue(mutableList)
            } ?: productSkuDetailsLiveData.postValue(emptyList())
        } else {
            productSkuDetailsLiveData.postValue(emptyList())
        }
    }

    override fun onPurchasesUpdated(billingResult: BillingResult, mutableList: MutableList<Purchase>?) {
        Log.e(TAG, "=== onPurchasesUpdated ===")
        Log.e(TAG, "result code : ${billingResult.responseCode}")
        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
            mutableList?.let {
                Log.e(TAG, "mutableList size : ${it.size}")
                purchaseUpdateLiveData.postValue(mutableList)
                handlePurchaseItem(mutableList)
            } ?: purchaseUpdateLiveData.postValue(emptyList())
        } else {
            purchaseUpdateLiveData.postValue(emptyList())
        }
    }

    fun queryAlreadyPurchases() {
        if (!checkBillingClient()) {
            Log.e(TAG, "queryPurchases: BillingClient is not ready")
        }
        Log.e(TAG, "queryPurchases: INAPP")
        val purchasedItems = arrayListOf<Purchase>()
        billingClient.queryPurchases(BillingClient.SkuType.INAPP)?.purchasesList?.let {
            purchasedItems.addAll(it)
        }
        Log.e(TAG, "purchasedItems: ${purchasedItems.toString()}")

        Log.e(TAG, "queryPurchases: SUBS")
        billingClient.queryPurchases(BillingClient.SkuType.SUBS)?.purchasesList?.let {
            purchasedItems.addAll(it)
        }
        Log.e(TAG, "purchasedItems: ${purchasedItems.toString()}")
        alreadyPurchasedLiveData.postValue(purchasedItems)
    }

    fun querySkuDetails(type: String, productList: List<String>) {
        if (!checkBillingClient()) {
            Log.e(TAG, "querySkuDetails: BillingClient is not ready / $type / ${productList.toString()}")
        }
        Log.e(TAG, "querySkuDetails")
        val params = SkuDetailsParams.newBuilder()
                .setType(type)
                .setSkusList(productList)
                .build()
        params?.let { skuDetailsParams ->
            Log.e(TAG, "querySkuDetailsAsync")
            billingClient.querySkuDetailsAsync(skuDetailsParams, this)
        }
    }

    fun launchBillingFlow(activity: Activity, params: BillingFlowParams): Int {
        val sku = params.sku
        val oldSku = params.oldSku
        Log.e(TAG, "launchBillingFlow: sku: $sku, oldSku: $oldSku")
        if (!checkBillingClient()) {
            Log.e(TAG, "launchBillingFlow: BillingClient is not ready")
        }
        val billingResult = billingClient.launchBillingFlow(activity, params)
        val responseCode = billingResult.responseCode
        val debugMessage = billingResult.debugMessage
        Log.e(TAG, "launchBillingFlow: BillingResponse $responseCode $debugMessage")
        return responseCode
    }

    fun handlePurchaseItem(items: List<Purchase>) {
        if (!checkBillingClient()) {
            return
        }
        Log.e(TAG, "handlePurchaseItem items: ${items.size}")
        for (item in items) {
            Log.e(TAG, "item type : ${item.sku}")
            when (item.sku) {
                in Constants.INAPP_PRODUCT_IDS -> {
                    consumePurchasePurchaseItem(item)
                }
                in Constants.SUBS_PRODUCT_IDS -> {
                    acknowledgePurchasePurchaseItem(item)
                }
                else -> {}
            }
        }
    }

    // 비소비성 상품 - 중복구매 불가
    fun acknowledgePurchasePurchaseItem(item: Purchase) {
        Log.e(TAG, "acknowledgePurchasePurchaseItem / ${item.purchaseToken}")
        val acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder()
                .setPurchaseToken(item.purchaseToken)
                .build()
        billingClient.acknowledgePurchase(acknowledgePurchaseParams) { billingResult ->
            val responseCode = billingResult.responseCode
            val debugMessage = billingResult.debugMessage
            Log.e(TAG, "acknowledgePurchase: $responseCode $debugMessage")
        }
    }

    // 소비성 상품 - 중복구매 가능
    fun consumePurchasePurchaseItem(item: Purchase) {
        Log.e(TAG, "consumePurchasePurchaseItem / ${item.purchaseToken}")
        val consumeParams = ConsumeParams.newBuilder()
                        .setPurchaseToken(item.purchaseToken)
                        .build()
        billingClient.consumeAsync(consumeParams) { billingResult, outToken ->
            val responseCode = billingResult.responseCode
            val debugMessage = billingResult.debugMessage
            Log.e(TAG, "consumePurchase: $responseCode $debugMessage / $outToken")

            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
            }
        }
    }

//
//    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
//    fun create() {
//        Log.e(TAG, "ON_CREATE")
//        // Create a new BillingClient in onCreate().
//        // Since the BillingClient can only be used once, we need to create a new instance
//        // after ending the previous connection to the Google Play Store in onDestroy().
//        billingClient = BillingClient.newBuilder(app.applicationContext)
//                .setListener(this)
//                .enablePendingPurchases() // Not used for subscriptions.
//                .build()
//        if (!checkBillingClient()) {
//            Log.e(TAG, "BillingClient: Start connection...")
//            billingClient.startConnection(this)
//        }
//    }
//
//    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
//    fun destroy() {
//        Log.e(TAG, "ON_DESTROY")
//        if (checkBillingClient()) {
//            Log.e(TAG, "BillingClient can only be used once -- closing connection")
//            // BillingClient can only be used once.
//            // After calling endConnection(), we must create a new BillingClient.
//            billingClient.endConnection()
//        }
//    }
}