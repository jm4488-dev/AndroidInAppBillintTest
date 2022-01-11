package com.jm4488.billingtest.coverpage

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.res.Configuration
import android.graphics.Rect
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ScrollView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.google.android.material.tabs.TabLayout
import com.jm4488.billingtest.R
import com.jm4488.billingtest.coverpage.adapter.CoverPageAutoScrollAdapter
import com.jm4488.billingtest.data.CoverPageModel
import com.jm4488.billingtest.databinding.FragmentCoverPageBinding
import com.jm4488.billingtest.utils.BaseBindingFragment
import com.jm4488.billingtest.utils.Config
import com.jm4488.retrofitservice.ApiCallback

class CoverPageFragment : BaseBindingFragment<FragmentCoverPageBinding>() {
    private lateinit var viewModel: CoverPageViewModel
    private lateinit var binding: FragmentCoverPageBinding
    private var repository = CoverPageRepository()

    private var secondLineCheck = false
    private var thirdLineCheck = false
    private var fourthLineCheck = false

    private var isBottomButtonVisible = false

    private var isLottiePlayed = false
    val headerHandler = Handler(Looper.getMainLooper())

    var currentIndexThirdPageItem = 0

    private var mDensity = 0f

    override fun initData() {
        viewModel = ViewModelProvider(this).get(CoverPageViewModel::class.java)
//        viewModel = ViewModelProviders.of(this).get(CoverPageViewModel::class.java)
        mDensity = requireContext().resources.displayMetrics.density
    }

    override fun getLayoutRes(): Int {
        return R.layout.fragment_cover_page
    }

    override fun initView(viewDataBinding: FragmentCoverPageBinding) {

        binding = viewDataBinding
        binding.lifecycleOwner = this.viewLifecycleOwner
        binding.coverPageViewModel = viewModel

        binding.scrollFrame.post {
            binding.scrollFrame.smoothScrollTo(0, binding.viewTopArea.top)
        }

        //
        addButtonClickListener(binding)

        repository.requestCoverPageJson(object : ApiCallback<CoverPageModel>() {
            override fun onSuccess(response: CoverPageModel?) {
                Log.e("[TEST]", "activity / response : ${response.toString()}")
                response?.let {
                    viewModel.coverJsonData.value = it
//                    viewModel.coverJsonData.postValue(it)
                    Log.e("[TEST]", "activity / viewModel.coverJsonData.value : ${viewModel.coverJsonData.value}")
                    viewModel.tabSize = it.div3_contents_count.getInt()
                    currentIndexThirdPageItem = it.div3_contents_count.getInt() * 4
                    startCoverPage()
                }
            }

            override fun onNotModified() {
            }

            override fun onFailed(throwable: Throwable?) {
            }

            override fun onFailed(code: Int, message: String) {
            }

        })
    }

    override fun onResume() {
        super.onResume()
        if (thirdLineCheck) {
            headerHandler.postDelayed(headerRunnable, 3000)
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        binding.rvThirdPageImages.visibility = View.INVISIBLE
        binding.rvThirdPageImages.smoothScrollToPosition(currentIndexThirdPageItem)
        binding.rvThirdPageImages.customAnimation(R.anim.slide_fade_in_upward_cover, View.VISIBLE, 1000)
    }

    override fun onPause() {
        super.onPause()
        headerHandler.removeCallbacks(headerRunnable)
    }

    fun startCoverPage() {
        // main whole scroll view setting
        setMainScrollView()

        // first page horizontal auto scroll view setting
        setFirstPageAutoScrollView()

        // second promotion lottie animation
        setSecondPageLottiePromotion()

        // third page view pager setting
        setThirdPageRecyclerView()
        setThirdPageTab()

        firstPageAnimationStart(binding)
    }

    private fun setMainScrollView() {
        binding.scrollFrame.viewTreeObserver.addOnScrollChangedListener {
            if (isBottomButtonVisible && binding.scrollFrame.isViewVisible(binding.btnCoverActionButton)) {
                isBottomButtonVisible = false
                binding.btnCoverBottomActionButton.customAnimation(R.anim.slide_fade_out_downward_cover, View.INVISIBLE)
            } else if (!isBottomButtonVisible && !binding.scrollFrame.isViewVisible(binding.btnCoverActionButton)) {
                isBottomButtonVisible = true
                binding.btnCoverBottomActionButton.customAnimation(R.anim.slide_fade_in_upward_cover, View.VISIBLE)
            }

            if (!secondLineCheck && binding.scrollFrame.isViewVisible(binding.viewSecondPageEndLine)) {
                secondLineCheck = true
                secondPageAnimationStart(binding)
            }
            if (!thirdLineCheck && binding.scrollFrame.isViewVisible(binding.viewThirdPageEndLine)) {
                thirdLineCheck = true
                thirdPageAnimationStart(binding)
            }
            if (!fourthLineCheck && binding.scrollFrame.isViewVisible(binding.viewFourthPageEndLine)) {
                fourthLineCheck = true
                fourthPageAnimationStart(binding)
            }
        }
    }

    private fun setFirstPageAutoScrollView() {
        val layoutManager = LinearLayoutManager(requireContext())
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        binding.asRecycleView.layoutManager = layoutManager

        val asAdapter = CoverPageAutoScrollAdapter(requireContext())
        asAdapter.items = viewModel.coverJsonData.value!!.div1_images_url

        binding.asRecycleView.adapter = asAdapter
        binding.asRecycleView.isLoopEnabled = true
        binding.asRecycleView.setCanTouch(false)
        binding.asRecycleView.openAutoScroll(40, false)
    }

    private fun setSecondPageLottiePromotion() {
        val lottieSecondPromotionFileName = if (Config.isTablet) "lottie_cover_2_promotion_t.json" else "lottie_cover_2_promotion_m.json"
        val lottieSecondPromotionFolderName = if (Config.isTablet) "lottie_cover_2_promotion_t" else "lottie_cover_2_promotion_m"
        binding.lottieSecondPagePromotion.setAnimation(lottieSecondPromotionFileName)
        binding.lottieSecondPagePromotion.imageAssetsFolder = lottieSecondPromotionFolderName
        binding.lottieSecondPagePromotion.layoutParams

        val lottieParams: ViewGroup.MarginLayoutParams = binding.lottieSecondPagePromotion.layoutParams as ViewGroup.MarginLayoutParams
        val tvParams: ViewGroup.MarginLayoutParams = binding.tvSecondPageUnregistTxt.layoutParams as ViewGroup.MarginLayoutParams
        if (Config.isTablet) {
            lottieParams.setMargins(0, (60 * mDensity).toInt(), 0, 0)
            tvParams.setMargins(0, (60 * mDensity).toInt(), 0, 0)
        } else {
            lottieParams.setMargins(0, (20 * mDensity).toInt(), 0, 0)
            tvParams.setMargins(0, (20 * mDensity).toInt(), 0, 0)
        }
        binding.lottieSecondPagePromotion.layoutParams = lottieParams
        binding.tvSecondPageUnregistTxt.layoutParams = tvParams
    }

    private fun setThirdPageRecyclerView() {
        val layoutManager = CenterLayoutManager(requireContext())
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        binding.rvThirdPageImages.layoutManager = layoutManager

        val asAdapter = CoverPageAutoScrollAdapter(requireContext(), false)
        var imagesList = arrayListOf<String>()
        for (con in viewModel.coverJsonData.value!!.div3_contents_define) {
            imagesList.add(con.poster_image_url)
        }
        asAdapter.items = imagesList

        binding.rvThirdPageImages.adapter = asAdapter
        binding.rvThirdPageImages.isLoopEnabled = true
        binding.rvThirdPageImages.pauseAutoScroll(true)
        binding.rvThirdPageImages.smoothScrollToPosition(currentIndexThirdPageItem)

        val size = resources.getDimensionPixelSize(R.dimen.margin_20dp)
        val deco = SpaceDecoration(size)
        binding.rvThirdPageImages.addItemDecoration(deco)

        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(binding.rvThirdPageImages)
        val listener = SnapPagerScrollListener(
            snapHelper,
            SnapPagerScrollListener.ON_SETTLED,
            true,
            object : SnapPagerScrollListener.OnChangeListener {
                override fun onSnapped(position: Int) {
                    currentIndexThirdPageItem = position
                    val reCalcPosition = currentIndexThirdPageItem % viewModel.tabSize
                    binding.tabThirdPageGenre.getTabAt(reCalcPosition)?.select()

                    headerHandler.removeCallbacks(headerRunnable)
                    headerHandler.postDelayed(headerRunnable, 3000)
                }
            }
        )
        binding.rvThirdPageImages.addOnScrollListener(listener)
    }

    private fun setThirdPageTab() {
        val defineContentsCount = viewModel.coverJsonData.value!!.div3_contents_count.getInt()
        for (index in 0 until defineContentsCount) {
            val txt = viewModel.coverJsonData.value!!.div3_contents_define[index].genre_text
            if (txt.isEmpty()) continue
            binding.tabThirdPageGenre.addTab(binding.tabThirdPageGenre.newTab().apply {
                text = txt
            })
        }

        binding.tabThirdPageGenre.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val selectedTabPosition = tab?.position ?: 0
                val movePosition = currentIndexThirdPageItem - ((currentIndexThirdPageItem % viewModel.tabSize) - selectedTabPosition)
                currentIndexThirdPageItem = movePosition
                binding.rvThirdPageImages.smoothScrollToPosition(currentIndexThirdPageItem)
                val reCalcPosition = currentIndexThirdPageItem % viewModel.tabSize
                rvItemInfoChangeAnimation(viewModel.coverJsonData.value!!.div3_contents_define[reCalcPosition])

                headerHandler.removeCallbacks(headerRunnable)
                headerHandler.postDelayed(headerRunnable, 3000)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })
    }

    private val headerRunnable = Runnable {
        currentIndexThirdPageItem += 1
        val reCalcPosition = currentIndexThirdPageItem % viewModel.tabSize
        binding.rvThirdPageImages.smoothScrollToPosition(currentIndexThirdPageItem)
        rvItemInfoChangeAnimation(viewModel.coverJsonData.value!!.div3_contents_define[reCalcPosition])
        binding.tabThirdPageGenre.getTabAt(reCalcPosition)?.select()
    }

    private fun firstPageAnimationStart(binding: FragmentCoverPageBinding) {
        binding.tvFirstPageMainTitle.customAnimation(R.anim.slide_fade_in_upward_cover, View.VISIBLE)
        binding.tvFirstPageSubTitle.customAnimation(R.anim.slide_fade_in_upward_cover, View.VISIBLE)
        binding.btnCoverActionButton.customAnimation(R.anim.fade_in_cover, View.VISIBLE)
        binding.asRecycleView.customAnimation(R.anim.fade_in_cover, View.VISIBLE)
    }

    private fun secondPageAnimationStart(binding: FragmentCoverPageBinding) {
        binding.tvSecondPageMainTxt.customAnimation(R.anim.slide_fade_in_upward_cover, View.VISIBLE)
        binding.tvSecondPageSubTxt.customAnimation(R.anim.slide_fade_in_upward_cover, View.VISIBLE)
        binding.tvSecondPageUnregistTxt.customAnimation(R.anim.slide_fade_in_upward_cover, View.VISIBLE)

        if (Config.isTablet) {
            lottiePromotionCircles()
        } else {
            nativePromotionCircles()
        }
    }

    private fun lottiePromotionCircles() {
        binding.groupPromotionCircles.visibility = View.GONE
        binding.lottieSecondPagePromotion.customAnimation(R.anim.slide_fade_in_upward_cover, View.VISIBLE)
        binding.lottieSecondPagePromotion.playAnimation()
    }

    private fun nativePromotionCircles() {
        binding.lottieSecondPagePromotion.visibility = View.GONE
        binding.ivSecondPageFirstCircle.customAnimation(R.anim.slide_fade_in_upward_cover, View.VISIBLE, 300)
        binding.ivSecondPageSecondCircle.customAnimation(R.anim.slide_fade_in_upward_cover, View.VISIBLE, 600)
        binding.ivSecondPageThirdCircle.customAnimation(R.anim.slide_fade_in_upward_cover, View.VISIBLE, 900)
    }

    private fun thirdPageAnimationStart(binding: FragmentCoverPageBinding) {
        binding.tvThirdPageMainTxt.customAnimation(R.anim.slide_fade_in_upward_cover, View.VISIBLE)
        binding.tvThirdPageSubTxt.customAnimation(R.anim.slide_fade_in_upward_cover, View.VISIBLE)
        binding.ivThirdPageBg.customAnimation(R.anim.slide_fade_in_upward_cover, View.VISIBLE)
        binding.rvThirdPageImages.customAnimation(R.anim.slide_fade_in_upward_cover, View.VISIBLE)
        binding.viewPagerItemInfo.viewCoverJsonInfo.customAnimation(R.anim.slide_fade_in_upward_cover, View.VISIBLE)
        binding.tabThirdPageGenre.customAnimation(R.anim.slide_fade_in_upward_cover, View.VISIBLE)
    }

    private fun fourthPageAnimationStart(binding: FragmentCoverPageBinding) {
        binding.tvFourthPageMainTxt.customAnimation(R.anim.slide_fade_in_upward_cover, View.VISIBLE)
        binding.tvFourthPageSubTxt.customAnimation(R.anim.slide_fade_in_upward_cover, View.VISIBLE)
        binding.ivFourthPageQuickvodBg.customAnimation(R.anim.slide_fade_in_upward_cover, View.VISIBLE)
        binding.lottieFourthPageTimeCounter.customAnimation(R.anim.slide_fade_in_upward_cover, View.VISIBLE)
        binding.lottieFourthPageTvmobileTransition.customAnimation(R.anim.slide_fade_in_upward_cover, View.VISIBLE)
        binding.tvPersonalPrivacy.customAnimation(R.anim.slide_fade_in_upward_cover, View.VISIBLE)

        if (!isLottiePlayed) {
            playLottieAnimation()
        }
    }

    private fun playLottieAnimation() {
        binding.lottieFourthPageTvmobileTransition.progress = 0f
        binding.lottieFourthPageTimeCounter.speed = 0.7f
        binding.lottieFourthPageTimeCounter.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator?) {}

            override fun onAnimationEnd(animation: Animator?) {
                binding.lottieFourthPageTvmobileTransition.playAnimation()
                binding.ivFourthPageQuickvodTag.customAnimation(R.anim.slide_fade_in_left_to_right_cover, View.VISIBLE, 500)
                binding.lottieFourthPageTimeCounter.customAnimation(R.anim.fade_out_cover)
            }

            override fun onAnimationCancel(animation: Animator?) {}

            override fun onAnimationRepeat(animation: Animator?) {}
        })

        Handler(Looper.getMainLooper()).postDelayed({
            binding.lottieFourthPageTimeCounter.playAnimation()
        }, 1000)
    }

    private fun rvItemInfoChangeAnimation(item: CoverPageModel.Contents) {
        binding.viewPagerItemInfo.viewCoverJsonInfo.apply {
            animate()
                .alpha(0f)
                .setDuration(500L)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        super.onAnimationEnd(animation)
                        viewModel.cover3ViewPagerItemInfo.value = item
                        binding.viewPagerItemInfo.viewCoverJsonInfo.apply {
                            animate()
                                .alpha(1f)
                                .setDuration(500L)
                                .setListener(null)
                        }
                    }

                })
        }
    }

    private fun ScrollView.isViewVisible(view: View): Boolean {
        val scrollBounds = Rect()
        this.getDrawingRect(scrollBounds)
        var top = 0f
        var temp = view
        while (temp !is ScrollView) {
            top += (temp).y
            temp = temp.parent as View
        }
        val bottom = top + view.height
        return scrollBounds.top < top && scrollBounds.bottom > bottom
    }

    private fun View.customAnimation(animId: Int, visible: Int = View.INVISIBLE, timer: Long = 0L) {
        Handler(Looper.getMainLooper()).postDelayed({
            this.startAnimation(AnimationUtils.loadAnimation(context, animId))
            this.visibility = visible
        }, timer)
    }

    private fun addButtonClickListener(binding: FragmentCoverPageBinding) {
        binding.tvLogin.setOnClickListener {
            Toast.makeText(requireContext(), "clicked / ${binding.tvLogin.text}", Toast.LENGTH_SHORT).show()
        }

        binding.btnCoverActionButton.setOnClickListener {
            Log.e("[TEST]", "tvLogin / ${viewModel.coverJsonData.value?.button1_text}")
            Toast.makeText(requireContext(), "clicked / ${binding.btnCoverActionButton.text}", Toast.LENGTH_SHORT).show()
        }

        binding.btnCoverBottomActionButton.setOnClickListener {
            Log.e("[TEST]", "tvLogin / ${viewModel.coverJsonData.value?.button2_text}")
            Toast.makeText(requireContext(), "clicked / ${binding.btnCoverBottomActionButton.text}", Toast.LENGTH_SHORT).show()
        }

        binding.tvPersonalPrivacy.setOnClickListener {
            Toast.makeText(requireContext(), "clicked / ${binding.tvPersonalPrivacy.text}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun String.getInt(): Int {
        return TextUtils.isDigitsOnly(this).let {
            when (it) {
                true -> this.toInt()
                false -> 0
            }
        }
    }

    private fun Int.valueToDp(): Int {
        return (this * requireContext().resources.displayMetrics.density).toInt()
    }
}