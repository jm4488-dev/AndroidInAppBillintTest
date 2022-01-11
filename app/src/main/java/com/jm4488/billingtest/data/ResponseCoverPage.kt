package com.jm4488.billingtest.data

import com.google.gson.annotations.SerializedName

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