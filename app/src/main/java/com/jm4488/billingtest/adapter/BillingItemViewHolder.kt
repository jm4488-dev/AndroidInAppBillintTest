package com.jm4488.billingtest.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.jm4488.billingtest.data.BillingProductItem
import com.jm4488.billingtest.data.PurchasedItem
import com.jm4488.billingtest.databinding.ItemBillingProductBinding
import com.jm4488.billingtest.databinding.ItemPurchasedProductBinding

open class BillingItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    // product item
    open fun onBind(data: BillingProductItem) {}
    class ProductViewHolder(private val binding: ItemBillingProductBinding) : BillingItemViewHolder(binding.root) {
        override fun onBind(data: BillingProductItem) {
            binding.data = data
        }
    }

    // already purchased item
    open fun onBind(data: PurchasedItem) {}
    class PurchasedViewHolder(private val binding: ItemPurchasedProductBinding) : BillingItemViewHolder(binding.root) {
        override fun onBind(data: PurchasedItem) {
            binding.data = data
        }
    }
}