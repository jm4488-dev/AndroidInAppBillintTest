package com.jm4488.billingtest.utils

import android.content.Context
import android.content.res.Configuration
import android.util.TypedValue
import java.util.regex.Pattern

object Utils {
    fun isTablet(context: Context): Boolean {
        var bTablet = false
        val screenSizeType = (context.resources.configuration.screenLayout
                and Configuration.SCREENLAYOUT_SIZE_MASK)
        when (screenSizeType) {
            Configuration.SCREENLAYOUT_SIZE_XLARGE, Configuration.SCREENLAYOUT_SIZE_LARGE -> bTablet =
                true
            else -> {}
        }
        return bTablet
    }

    fun resizeImagePath(originUrl: String, level: Int): String {
        return try {
            val extention =
                originUrl.substring(originUrl.lastIndexOf('.'), originUrl.length)
            (originUrl.substring(0, originUrl.lastIndexOf('.'))
                    + "_" + Integer.toString(level) + extention)
        } catch (e: Exception) {
            originUrl
        }
    }

    fun resizeImagePath(url: String, width: Int, useWebp: Boolean): String {
        return resizeImagePath(url, width, 0, useWebp)
    }

    fun resizeImagePath(url: String, width: Int, height: Int, useWebp: Boolean): String {
        var resizeUrl = ""
        val regex = "(.*/)*.+\\.(png|jpg|gif|bmp|jpeg|PNG|JPG|GIF|BMP)"
        try {
            val p = Pattern.compile(regex)
            val matcher = p.matcher(url)
            var extension = ""
            while (matcher.find()) {
                extension = "." + matcher.group(2)
            }
            resizeUrl = (url.substring(0, url.indexOf(extension))
                    + "_w" + width)
            if (height > 0) resizeUrl += "_h$height"
            val quality = "_q75" // fixed 75
            /*if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
                quality = "_q75";
            } else {
                quality = "_q20";
            }*/resizeUrl += quality
            val urlParams = url.substring(url.indexOf(extension), url.length).replace(extension, "")
            resizeUrl += if (false /*useWebp*/) {
                ".webp$urlParams"
            } else {
                extension + urlParams
            }
        } catch (e: Exception) {
            resizeUrl = url
        }
        return resizeUrl
    }

    fun getPixelToDp(context: Context, dp: Int): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(),
            context.resources.displayMetrics
        ).toInt()
    }
}