package com.jm4488.billingtest.adapter

import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingFlowParams
import com.jm4488.billingtest.data.BillingProductItem
import com.jm4488.billingtest.data.PurchasedItem
import com.jm4488.billingtest.databinding.ItemBillingProductBinding
import com.jm4488.billingtest.databinding.ItemPurchasedProductBinding
import kotlinx.android.synthetic.main.item_billing_product.view.*
import kotlinx.android.synthetic.main.item_purchased_product.view.*

class PurchasedItemAdapter(activity: Activity) : RecyclerView.Adapter<BillingItemViewHolder>() {
    var items = arrayListOf<PurchasedItem>()
    private lateinit var billingClient: BillingClient
    var activity = activity

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BillingItemViewHolder {
        return BillingItemViewHolder.PurchasedViewHolder(ItemPurchasedProductBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun settingBillingClient(client: BillingClient) {
        billingClient = client
    }

    override fun onBindViewHolder(holder: BillingItemViewHolder, position: Int) {
        holder.onBind(items[position])
        holder.itemView.btn_refund.setOnClickListener {
            Log.e("[TEST]", "btn_refund click")
        }
    }
}