package com.jm4488.billingtest.billing

import com.jm4488.retrofitservice.Base.BaseResponse

/*
// 소모 상품
{
  "orderId":"GPA.0000-...",
  "packageName":"com.앱 패키지 이름",
  "productId":"test_002",
  "purchaseTime":1594086304413,
  "purchaseState":0,
  "purchaseToken":"tokenvalue.A1-bbbbb...",
  "acknowledged":false
}
// 구독 상품
{
  "orderId":"GPA.1111-...",
  "packageName":"com.앱 패키지 이름",
  "productId":"test_sub_002",
  "purchaseTime":1594343716114,
  "purchaseState":0,
  "purchaseToken":"tokenvalue.A0-aaaaa...",
  "autoRenewing":true,
  "acknowledged":false
}
* */

data class InAppBillingModel(
    var orderId: String,
    var packageName: String,
    var productId: String,
    var purchaseTime: String,
    var purchaseState: String,
    var purchaseToken: String,
    var acknowledged: Boolean
) : BaseResponse()

data class SubscribeBillingModel(
    var orderId: String,
    var packageName: String,
    var productId: String,
    var purchaseTime: String,
    var purchaseState: String,
    var purchaseToken: String,
    var autoRenewing: Boolean,
    var acknowledged: Boolean
) : BaseResponse()

