package com.jm4488.billingtest.utils

import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.jm4488.billingtest.R

@BindingAdapter("visibleGone")
fun setVisibility(view: View, visible: Boolean) {
    view.visibility = if (visible) View.VISIBLE else View.GONE
}

@BindingAdapter(value = ["imageUrl", "sizeWidth", "sizeHeight"], requireAll = false)
fun loadImage(imageView: ImageView, url: String?, width: Int, height: Int) {
    var errorRes = 0
    if (width < height) errorRes =
        R.drawable.img_default_thum_1 else if (width > height) errorRes =
        R.drawable.img_default_thum_2
    if (TextUtils.isEmpty(url)) {
        imageView.setImageResource(errorRes)
    } else {
        val imageWidth: Int
        val imageHeight: Int
        if (Config.isTablet) {
            imageWidth = (width * 1.2).toInt()
            imageHeight = (height * 1.2).toInt()
        } else {
            imageWidth = width
            imageHeight = height
        }
        val resizeUrl: String = Utils.resizeImagePath(
            url!!,
            Utils.getPixelToDp(imageView.context, imageWidth),
            Utils.getPixelToDp(imageView.context, imageHeight), false
        )
        ImageMgr.getInstance().displayImage(imageView.context, resizeUrl, imageView, errorRes, 0)
    }
}