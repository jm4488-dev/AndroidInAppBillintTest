package com.jm4488.billingtest.data

import com.google.gson.annotations.SerializedName
import java.io.Serializable

open class ResponseBase : Serializable {
    @SerializedName("resultcode")
    private var resultCode = RETURN_CODE_200

    @SerializedName("resultmessage")
    private var resultMessage: String? = ""

    @SerializedName("resultoptional")
    var resultOptional: String? = null

    constructor(code: Int, msg: String?) {
        resultCode = code
        resultMessage = msg
    }

    constructor(code: Int, msg: String?, optional: String?) {
        resultCode = code
        resultMessage = msg
        resultOptional = optional
    }

    constructor() {}

    fun getResultCode(): Int {
        if (resultCode == 0) // todo 임시로 일단!
            resultCode = RETURN_CODE_200
        return resultCode
    }

    fun setResultCode(code: Int) {
        resultCode = code
    }

    fun getResultMessage(): String {
        //return resultMessage;
        return if (resultMessage == null) "" else resultMessage!!.replace("\\n", "\n")
    }

    fun setResultMessage(msg: String?) {
        resultMessage = msg
    }

    val isSuccess: Boolean
        get() = if (resultCode == 0) true else RETURN_CODE_200 == resultCode

    override fun toString(): String {
        return "ResponseBase{" +
                "resultCode=" + resultCode +
                ", resultMessage='" + resultMessage + '\'' +
                ", resultOptional='" + resultOptional + '\'' +
                '}'
    }

    companion object {
        //api response return code
        const val RETURN_CODE_200 = 200

        // 로그인시 처리
        // * httpcode:550 / resultcode : 210 - resultmessage: 입력하신 정보에 해당하는 계정을 찾을 수 없습니다.\nID, PW를 확인해 주세요.
        //     * httpcode:551 / resultcode : 301 - credential 로그인 실패 / resultmessage:계정정보가 변경되어 자동로그인이 해제되었습니다.\n다시 로그인 해주세요. - 클라이언트 : 로그인 화면으로 이동
        //     * httpcode:551 / resultcode : 302 - SNS 회원가입 필요 / 메시지 출력 후 가입 url로 이동"
        const val RETURN_CODE_210 = 210
        const val RETURN_CODE_301 = 301
        const val RETURN_CODE_302 = 302
        const val RETURN_CODE_502 = 502 //Azure freezing 시 일시적으로 응답
        const val RETURN_CODE_550 = 550 //메시지 표시 수준 에러 (에러 메시지를 노출하고 더 이상의 작업은 없음)
        const val RETURN_CODE_551 =
            551 //에러메시지를 노출하지 않고 추가 작업 없음     <<<< 20180307 변경됨 //추가 동작 필요 수준 에러 (에러 메시지를 노출하고 추가 작업이 있음)
        const val RETURN_CODE_999 = 999 // 임의 값
    }
}