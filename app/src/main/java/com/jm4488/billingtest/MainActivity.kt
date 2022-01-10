package com.jm4488.billingtest

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jm4488.billingtest.googlebilling.GoogleBillingActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()

        btn_google_billing_test.setOnClickListener {
            val intent = Intent(this, GoogleBillingActivity::class.java)
            this.startActivity(intent)
        }

        btn_cover_page.setOnClickListener {

        }

    }

}