package com.jm4488.billingtest.coverpage

import android.content.Context
import android.util.DisplayMetrics
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView

class CenterLayoutManager(val context: Context) : LinearLayoutManager(context) {
    private val mShrinkAmount = 0.5f
    private val mShrinkDistance = 0.99f

    companion object {
        private const val MILLISECONDS_PER_INCH = 50f //default is 25f (bigger = slower)
    }

    override fun smoothScrollToPosition(recyclerView: RecyclerView, state: RecyclerView.State, position: Int) {
        val linearSmoothScroller: LinearSmoothScroller = object : LinearSmoothScroller(recyclerView.context) {
            override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics): Float {
                return MILLISECONDS_PER_INCH / displayMetrics.densityDpi
            }

            override fun calculateDtToFit(viewStart: Int, viewEnd: Int, boxStart: Int, boxEnd: Int, snapPreference: Int): Int {
                return boxStart + (boxEnd - boxStart) / 2 - (viewStart + (viewEnd - viewStart) / 2)
            }
        }
        linearSmoothScroller.targetPosition = position
        startSmoothScroll(linearSmoothScroller)
    }

    override fun scrollHorizontallyBy(dx: Int, recycler: RecyclerView.Recycler?, state: RecyclerView.State?): Int {
        val orientation = orientation
        return if (orientation == HORIZONTAL) {
            val scrolled = super.scrollHorizontallyBy(dx, recycler, state)
            val midpoint = width / 2f
            val d0 = 0f
            val d1: Float = mShrinkDistance * midpoint
            val s0 = 1f
            val s1: Float = 1f - mShrinkAmount
            for (i in 0 until childCount) {
                val child = getChildAt(i)
                val childMidpoint = (getDecoratedRight(child!!) + getDecoratedLeft(child)) / 2f
//                    val d = Math.min(d1, Math.abs(midpoint - childMidpoint))
                val d = Math.abs(midpoint - childMidpoint)
                val scale = s0 + (s1 - s0) * (d - d0) / (d1 - d0)
                child.alpha = scale
            }
            scrolled
        } else {
            0
        }
    }
}