package com.jm4488.billingtest.network

import com.jm4488.billingtest.billing.InAppBillingModel
import com.jm4488.billingtest.coverpage.ResponseCoverPage
import retrofit2.Call
import retrofit2.http.*

interface WavveServer {
//    @GET("users/current/durations")
//    fun getCodingTime(
//        @Query("date") date : String,
//        @Query("api_key") string : String
//    ): Call<RawResponseData>

    // https://apis-sg.wavve.com/purchase/iap/google
    // ?apikey=E5F3E0D30947AA5440556471321BB6D9
    // &credential=XRIextSiEr2lGvdexpQ88Xx0AzZMIiav%2BnGY6Wr0qbmuW9jIY%2ByGpQBWoexLBkFcn4uwKWovLFqtaz1%2FtczJxZqpaDHLUasE3cRhni7l8u11b2DsVhnVZf6qytMDOdAmoiIiqNE8XnGeFw8NrVKngpImHkNy776RACBLvP2oELvRjsgUVvo%2BJ%2FsnpqU8Tmnze6c45lKS4DsUDSxTcpEB79FfR0AyeYwXJzj3As7FxIIsLxxuIVtCynLk%2F%2FNLWYz5
    // &device=pc
    // &partner=pooq&pooqzone=none
    // &region=kor
    // &drm=wm
    // &targetage=auto
    // &iaptype=purchase

    // packageName, productId, purchaseToken
    @Headers("Content-Type:application/json")
    @POST("purchase/iap/google")
    fun checkReceipt(
        @Body body: String
    ): Call<InAppBillingModel>

//    @PUT("user/basic")
//    fun uploadBasicInfo(@Query("email") email : String,
//                        @Query("age") age : Int,
//                        @Query("gender") gender : Int,
//                        @Query("one_line") one_line : String?
//    ): Call<DataModel.PutResponse>


    //
    @GET
    open fun requestCoverPage(
        @Url json: String,
        @Query("timestamp") timestamp: String
    ): Call<ResponseCoverPage>
}