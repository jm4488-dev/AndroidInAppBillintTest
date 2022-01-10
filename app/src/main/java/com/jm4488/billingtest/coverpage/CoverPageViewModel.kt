package com.jm4488.billingtest.coverpage

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.annotations.SerializedName
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

data class ResponseCoverPage(
    var code: Int,
    var msg: String,
    @SerializedName("cover_page")
    var coverPage: CoverPageModel?
) : ResponseBase(code, msg) {
    constructor(resultCode: Int, resultMsg: String) : this(resultCode, resultMsg, null)
    constructor(coverObj: CoverPageModel) : this(200, "", coverObj)
    constructor(resultMsg: String) : this(500, resultMsg, null)
}

data class CoverPageModel(
    @SerializedName("button1_text")
    var button1_text: String = "",
    @SerializedName("button2_text")
    var button2_text: String = "",
    @SerializedName("div1_images_count")
    var div1_images_count: String = "",
    @SerializedName("div1_images_url")
    var div1_images_url: ArrayList<String> = arrayListOf(),
    @SerializedName("div3_contents_count")
    var div3_contents_count: String = "",
    @SerializedName("div3_contents_list_text")
    var div3_contents_list_text: ArrayList<String> = arrayListOf(),
    @SerializedName("div3_contents_define")
    var div3_contents_define: ArrayList<Contents> = arrayListOf(),
    @SerializedName("div4_image_url")
    var div4_image_url: String = "",
    @SerializedName("quickvod_icon_image_url")
    var quickvod_icon_image_url: String = ""
) {
    data class Contents(
        @SerializedName("order_no")
        var order_no: String = "",
        @SerializedName("content_type")
        var content_type: String = "",
        @SerializedName("genre_text")
        var genre_text: String = "",
        @SerializedName("poster_image_url")
        var poster_image_url: String = "",
        @SerializedName("detail_image_url")
        var detail_image_url: String = "",
        @SerializedName("detail_text")
        var detail_text: Detail = Detail()
    )

    class Detail(
        @SerializedName("programtitle")
        var programTitle: String = "",
        @SerializedName("episodenumber")
        var episodeNumber: String = "",
        @SerializedName("title")
        var title: String = "",
        @SerializedName("rating")
        var rating: String = "",
        @SerializedName("releasedate")
        var releaseDate: String = "",
        @SerializedName("releaseweekday")
        var releaseWeekday: String = "",
        @SerializedName("genretext")
        var genreText: String = "",
        @SerializedName("playtime")
        var playtime: String = "",
        @SerializedName("playtimetext")
        var playtimetext: String = "",
        @SerializedName("targetage")
        var targetAge: String = ""
    ) {
        fun getEpisodeStr(): String {
            if (episodeNumber.isEmpty()) return ""
            return "${episodeNumber}회"
        }

        fun getRatingStr(): String {
            if (rating.isEmpty()) return ""
            return "평점 $rating"
        }

        fun getCombineDateWeek(): String {
            if (releaseDate.isEmpty() || releaseWeekday.isEmpty()) return ""
            return "${releaseDate}(${releaseWeekday})"
        }

        fun getAgeStr(): String {
            if (targetAge.isEmpty()) return ""
            return "${targetAge}세"
        }

        fun isDateWeekVisible(txt: String = ""): Boolean {
            if (txt.isEmpty()) return false
            return true
        }
    }
}