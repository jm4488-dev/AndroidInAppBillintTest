package com.jm4488.billingtest.coverpage.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.jm4488.billingtest.data.CoverPageModel
import com.jm4488.billingtest.databinding.ItemCover1BannerAutoscrollBinding
import com.jm4488.billingtest.databinding.ItemCover3ContentsRvBinding

open class CoverPageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    open fun onBind(data: String) {}
    open fun onBind(data: CoverPageModel.Contents) {}

    class AutoScrollViewHolder(val binding: ItemCover1BannerAutoscrollBinding) : CoverPageViewHolder(binding.root) {
        override fun onBind(data: String) {
            binding.imgUrl = data
        }
    }

    class ContentsRecyclerHolder(val binding: ItemCover3ContentsRvBinding) : CoverPageViewHolder(binding.root) {
        override fun onBind(data: String) {
            binding.imgUrl = data
        }
    }
}