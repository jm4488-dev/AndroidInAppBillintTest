package com.jm4488.billingtest.coverpage

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jm4488.billingtest.R

class CoverPageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cover_page)
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.container, CoverPageFragment(), "")
            transaction.commitAllowingStateLoss()
        } else {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.container, CoverPageFragment(), "")
            transaction.commitAllowingStateLoss()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}