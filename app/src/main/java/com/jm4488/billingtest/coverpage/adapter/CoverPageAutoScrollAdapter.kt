package com.jm4488.billingtest.coverpage.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jm4488.billingtest.databinding.ItemCover1BannerAutoscrollBinding
import com.jm4488.billingtest.databinding.ItemCover3ContentsRvBinding
import com.jm4488.billingtest.utils.Config
import com.jm4488.billingtest.utils.Utils

class CoverPageAutoScrollAdapter() : RecyclerView.Adapter<CoverPageViewHolder>() {
    private var MARGIN_SIDE = 16
    var items = arrayListOf<String>()
    var isMargin = false

    private lateinit var context: Context

    constructor(context: Context, isMargin: Boolean = true) : this() {
        this.context = context
        if (Config.isTablet) MARGIN_SIDE = 30
        this.isMargin = isMargin
    }

    override fun getItemViewType(position: Int): Int {
        return when (this.isMargin) {
            true -> 0
            false -> 1
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoverPageViewHolder {
        return when (viewType) {
            0 -> CoverPageViewHolder.AutoScrollViewHolder(
                ItemCover1BannerAutoscrollBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            else -> CoverPageViewHolder.ContentsRecyclerHolder(
                ItemCover3ContentsRvBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }
    }

    override fun onBindViewHolder(holder: CoverPageViewHolder, position: Int) {
        holder.onBind(items[position])
        if (!isMargin) {
            return
        }

        val params: ViewGroup.MarginLayoutParams = holder.itemView.layoutParams as ViewGroup.MarginLayoutParams
        if (!Config.isTablet) {
            params.width = convertValueToDP(124)
            params.height = convertValueToDP(220)
        }
        when (position % 6) {
            0 -> params.setMargins(convertValueToDP(MARGIN_SIDE), convertValueToDP(0), convertValueToDP(MARGIN_SIDE), convertValueToDP(0))
            1 -> params.setMargins(convertValueToDP(MARGIN_SIDE), convertValueToDP(120), convertValueToDP(MARGIN_SIDE), convertValueToDP(0))
            2 -> params.setMargins(convertValueToDP(MARGIN_SIDE), convertValueToDP(40), convertValueToDP(MARGIN_SIDE), convertValueToDP(0))
            3 -> params.setMargins(convertValueToDP(MARGIN_SIDE), convertValueToDP(90), convertValueToDP(MARGIN_SIDE), convertValueToDP(0))
            4 -> params.setMargins(convertValueToDP(MARGIN_SIDE), convertValueToDP(30), convertValueToDP(MARGIN_SIDE), convertValueToDP(0))
            5 -> params.setMargins(convertValueToDP(MARGIN_SIDE), convertValueToDP(70), convertValueToDP(MARGIN_SIDE), convertValueToDP(0))
        }
        holder.itemView.layoutParams = params
    }

    override fun getItemCount(): Int = items.size

    private fun convertValueToDP(value: Int): Int {
        val d = context.resources.displayMetrics.density
        return (value * d).toInt()
    }
}