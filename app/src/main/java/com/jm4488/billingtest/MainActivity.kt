package com.jm4488.billingtest

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.android.billingclient.api.*
import com.jm4488.billingtest.activity.BillingPurchaseActivity
import com.jm4488.billingtest.activity.BillingSubscribeActivity
import com.jm4488.billingtest.adapter.BillingItemViewHolder
import com.jm4488.billingtest.adapter.PurchasedItemAdapter
import com.jm4488.billingtest.billing.BillingViewModel
import com.jm4488.billingtest.billing.InAppBillingModel
import com.jm4488.billingtest.databinding.ActivityMainBinding
import com.jm4488.billingtest.network.NetworkParam
import com.jm4488.billingtest.network.WavveServer
import com.jm4488.billingtest.utils.GoogleBillingUtils
import com.jm4488.retrofitservice.RestfulService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var billingUtils: GoogleBillingUtils
    private lateinit var billingViewModel: BillingViewModel

    private lateinit var binding: ActivityMainBinding
    private lateinit var purchasedAdapter: PurchasedItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()
        billingViewModel = ViewModelProviders.of(this).get(BillingViewModel::class.java)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        billingUtils = (application as GlobalApplication).googleBillingUtils
//        lifecycle.addObserver(billingUtils)

        init()

        billingUtils.alreadyPurchasedLiveData.observe(this, Observer { purchases ->
            purchases?.let { list ->
                makeAlreadyPurchasedList(list)
            }
        })

        billingUtils.consumeCompleteLiveData.observe(this, Observer { consumedItem ->
            consumedItem?.let { item ->
                purchasedAdapter.updateConsumedItem(item)
            }
        })

        billingUtils.acknowledgeCompleteLiveData.observe(this, Observer { acknowledgedItem ->
            acknowledgedItem?.let { item ->
                val position = purchasedAdapter.getPosition(item)
                if (position != -1) {
                    val view = binding.rvProductList.findViewHolderForAdapterPosition(position)
                    view?.let {
                        purchasedAdapter.updateAcknowledgedItem(it as BillingItemViewHolder)
                    }
                }
            }
        })

        binding.groupLoading.visibility = View.VISIBLE
        Handler().postDelayed({
            billingUtils.queryAlreadyPurchases()
        }, 500)
    }

    private fun init() {
        binding.vm = billingViewModel

        purchasedAdapter = PurchasedItemAdapter()
        purchasedAdapter.setUtil(billingUtils)

        binding.rvProductList.layoutManager = LinearLayoutManager(this)
        binding.rvProductList.itemAnimator?.let {
            when (it) {
                is SimpleItemAnimator -> it.supportsChangeAnimations = false
            }
        }
        binding.rvProductList.adapter = purchasedAdapter

        binding.btnAlreadyPurchasedList.setOnClickListener {
            Log.e("[MAINACT]", "=== btn_load_product Click ===")
            billingUtils.queryAlreadyPurchases()
        }

        binding.btnGoPurchaseProduct.setOnClickListener {
            startActivity(Intent(baseContext, BillingPurchaseActivity::class.java))
        }

        binding.btnGoSubscribeProduct.setOnClickListener {
            startActivity(Intent(baseContext, BillingSubscribeActivity::class.java))
        }

        binding.btnLogout.setOnClickListener {
            // https://apis-sg.wavve.com/purchase/iap/google
            // ?apikey=E5F3E0D30947AA5440556471321BB6D9
            // &credential=XRIextSiEr2lGvdexpQ88Xx0AzZMIiav%2BnGY6Wr0qbmuW9jIY%2ByGpQBWoexLBkFcn4uwKWovLFqtaz1%2FtczJxZqpaDHLUasE3cRhni7l8u11b2DsVhnVZf6qytMDOdAmoiIiqNE8XnGeFw8NrVKngpImHkNy776RACBLvP2oELvRjsgUVvo%2BJ%2FsnpqU8Tmnze6c45lKS4DsUDSxTcpEB79FfR0AyeYwXJzj3As7FxIIsLxxuIVtCynLk%2F%2FNLWYz5
            // &device=pc
            // &partner=pooq
            // &pooqzone=none
            // &region=kor
            // &drm=wm
            // &targetage=auto
            // &iaptype=purchase

            val paramMap = NetworkParam.Builder().build().getNetworkParamsMap()
            val wavveApi: WavveServer = RestfulService.getInstance().getApiInstance(paramMap, WavveServer::class.java)
            val service: Call<InAppBillingModel> = wavveApi.checkReceipt("com.jm4488.billingtest", "test_sub_001", "")
            service.enqueue(object : Callback<InAppBillingModel?> {
                override fun onResponse(call: Call<InAppBillingModel?>, response: Response<InAppBillingModel?>) {
                    Log.e("[Network]", "onResponse : $response")
                }

                override fun onFailure(call: Call<InAppBillingModel?>, t: Throwable) {
                    Log.e("[Network]", "onFailure : ${t.localizedMessage}")
                }
            })
        }
    }

    private fun makeAlreadyPurchasedList(purchasedItems: List<Purchase>) {
        Log.e("[MAINACT]", "=== makeAlreadyPurchasedList ===")
        binding.groupLoading.visibility = View.GONE
        purchasedAdapter.items.clear()
        purchasedAdapter.items = ArrayList(purchasedItems)
//        purchasedAdapter.items = ArrayList(emptyList())
        purchasedAdapter.notifyDataSetChanged()
    }
}