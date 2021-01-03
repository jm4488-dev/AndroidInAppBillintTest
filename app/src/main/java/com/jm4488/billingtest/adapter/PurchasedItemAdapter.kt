package com.jm4488.billingtest.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.billingclient.api.Purchase
import com.jm4488.billingtest.databinding.ItemPurchasedProductBinding
import kotlinx.android.synthetic.main.item_purchased_product.view.*

class PurchasedItemAdapter() : RecyclerView.Adapter<BillingItemViewHolder>() {
    var items = arrayListOf<Purchase>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BillingItemViewHolder {
        return BillingItemViewHolder.PurchasedViewHolder(ItemPurchasedProductBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: BillingItemViewHolder, position: Int) {
        holder.onBind(items[position])
        holder.itemView.btn_refund.setOnClickListener {
            Log.e("[PURCHASEDADAP]", "btn_refund click")
        }
    }
}