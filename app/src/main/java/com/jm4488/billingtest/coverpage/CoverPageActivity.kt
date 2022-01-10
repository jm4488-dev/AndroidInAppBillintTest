package com.jm4488.billingtest.coverpage

import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.jm4488.billingtest.databinding.ActivityCoverPageBinding

class CoverPageActivity : AppCompatActivity() {

    private lateinit var viewModel: CoverPageViewModel
    private lateinit var binding: ActivityCoverPageBinding
    private var repository = CoverPageRepository()

    private var secondLineCheck = false
    private var thirdLineCheck = false
    private var fourthLineCheck = false

    private var isBottomButtonVisible = false

    private var isLottiePlayed = false
    val headerHandler = Handler(Looper.getMainLooper())

    var currentIndexThirdPageItem = 0

    private var mDensity = 0f

}