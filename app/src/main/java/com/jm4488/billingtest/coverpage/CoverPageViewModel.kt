package com.jm4488.billingtest.coverpage

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.annotations.SerializedName
import com.jm4488.billingtest.data.CoverPageModel
import com.jm4488.billingtest.data.ResponseBase

class CoverPageViewModel : ViewModel() {

    var titleText = "wavve\n재미의 파도를 타다."
    var subTitleText = "드라마부터 예능까지\n미드부터 영화까지"
    var secondPageMainText = "신규 회원은\n첫 달 100원"
    var secondPageSubText = "추가 2개월 50% 할인 혜택까지!"
    var secondPageUnRegistText = "언제든지 해지신청 가능합니다."
    var thirdPageMainText = "영화, 미드, 예능\n모든 콘텐츠를 한번에"
    var thirdPageSubText = "30만편 이상의 VOD\n독점 해외 시리즈부터 신작 영화까지!"
    var fourthPageMainText = "세상에서 가장 빠른\n다시보기"
    var fourthPageSubText = "Quick VOD로 본방 시작 5분 만에\n내 시간에 맞춰 On-Air"

    var coverJsonData = MutableLiveData<CoverPageModel>()
    var tabSize = 0
    var cover3ViewPagerItemInfo = MutableLiveData<CoverPageModel.Contents>()

    init {
        coverJsonData.postValue(CoverPageModel())
        cover3ViewPagerItemInfo.postValue(CoverPageModel.Contents())
    }

}