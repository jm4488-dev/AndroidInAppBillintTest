package com.jm4488.billingtest.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomViewTarget
import com.bumptech.glide.request.target.NotificationTarget
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import java.util.logging.Logger


object ImageMgr {
    private var instance: ImageMgr? = null
    private val cacheStrategy: DiskCacheStrategy = DiskCacheStrategy.RESOURCE

    fun getInstance(): ImageMgr {
        if (instance == null) {
            instance = ImageMgr
        }
        return instance as ImageMgr
    }

    private fun getImageUrl(url: String, isSMR: Boolean): String {
        var url = url
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            url = if (isSMR || Build.VERSION.SDK_INT <= 19) { // smr 은 http:// , postfix 도 문의 할것!
                "http://$url"
            } else {
                "https://$url"
            }
        }
        return url
    }

    /**
     * 종료시 모듈 초기화
     */
    fun onDestroy() {
        instance = null
    }

    fun clearCache(context: Context) {
        //GlideApp.get(context).clearDiskCache();
    }

    /* README! context 에 application (appData) 넣지 마세요~ */
    fun displayImage(
        context: Context,
        url: String,
        view: ImageView,
        errorImgRes: Int,
        holderResource: Int
    ) {

        // 오류 예외처리 edit by hyk
        var url = url
        if (context is AppCompatActivity) {
            if (context.isFinishing) return
        }
        if (view == null) return
        if (TextUtils.isEmpty(url)) {
            view.setImageResource(errorImgRes)
            return
        }
        val options: RequestOptions = RequestOptions()
            .diskCacheStrategy(cacheStrategy)
            .error(errorImgRes)
            .dontAnimate()
        if (errorImgRes > 0) options.error(errorImgRes)
        if (holderResource > 0) options.placeholder(holderResource)
        url = getImageUrl(url, false)
        Glide.with(context)
            .load(url)
            .apply(options)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(view)
    }

}