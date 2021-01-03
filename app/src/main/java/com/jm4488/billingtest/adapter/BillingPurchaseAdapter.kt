package com.jm4488.billingtest.adapter

import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingFlowParams
import com.jm4488.billingtest.data.BillingProductItem
import com.jm4488.billingtest.databinding.ItemBillingProductBinding
import kotlinx.android.synthetic.main.item_billing_product.view.*

class BillingPurchaseAdapter(activity: Activity) : RecyclerView.Adapter<BillingItemViewHolder>() {
    var items = arrayListOf<BillingProductItem>()
    private lateinit var billingClient: BillingClient
    var activity = activity

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BillingItemViewHolder {
        return BillingItemViewHolder.ProductViewHolder(ItemBillingProductBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun settingBillingClient(client: BillingClient) {
        billingClient = client
    }

    override fun onBindViewHolder(holder: BillingItemViewHolder, position: Int) {
        holder.onBind(items[position])
        holder.itemView.btn_buy.setOnClickListener {
            items[position].skuDetailsItem?.let {
                Log.e("[TEST]", "skuDetailsItem desc : ${it.toString()}")

                val billingFlowParams = BillingFlowParams.newBuilder()
                    .setSkuDetails(it)
                    .build()

                when (billingClient.launchBillingFlow(activity, billingFlowParams).responseCode) {
                    BillingClient.BillingResponseCode.BILLING_UNAVAILABLE -> {
                        Log.e("[TEST]", "launchBillingFlow response : BILLING_UNAVAILABLE")
                    }
                    BillingClient.BillingResponseCode.DEVELOPER_ERROR -> {
                        Log.e("[TEST]", "launchBillingFlow response : DEVELOPER_ERROR")
                    }
                    BillingClient.BillingResponseCode.FEATURE_NOT_SUPPORTED -> {
                        Log.e("[TEST]", "launchBillingFlow response : FEATURE_NOT_SUPPORTED")
                    }
                    BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED -> {
                        Log.e("[TEST]", "launchBillingFlow response : ITEM_ALREADY_OWNED")
                    }
                    BillingClient.BillingResponseCode.SERVICE_DISCONNECTED -> {
                        Log.e("[TEST]", "launchBillingFlow response : SERVICE_DISCONNECTED")
                    }
                    BillingClient.BillingResponseCode.SERVICE_TIMEOUT -> {
                        Log.e("[TEST]", "launchBillingFlow response : SERVICE_TIMEOUT")
                    }
                    BillingClient.BillingResponseCode.ITEM_UNAVAILABLE -> {
                        Log.e("[TEST]", "launchBillingFlow response : ITEM_UNAVAILABLE")
                    }
                    else -> {
                        Log.e("[TEST]", "launchBillingFlow response : else")
                    }
                }
            }
        }
    }
}