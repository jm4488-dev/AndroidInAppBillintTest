package com.jm4488.billingtest.network

class NetworkParam {

    companion object {
        val APIKEY = "E5F3E0D30947AA5440556471321BB6D9"
        val baseUrl = "https://apis-sg.wavve.com/"
    }

    val CREDENTIAL = "credential"
    val DEVICE = "device"
    val PARTNER = "partner"
    val POOQZONE = "pooqzone"
    val REGION = "region"
    val DRM = "drm"
    val TARGETAGE = "targetage"
    val IAPTYPE = "iaptype"

    var credentialValue = "XRIextSiEr2lGvdexpQ88Xx0AzZMIiav%2BnGY6Wr0qbmuW9jIY%2ByGpQBWoexLBkFcn4uwKWovLFqtaz1%2FtczJxZqpaDHLUasE3cRhni7l8u11b2DsVhnVZf6qytMDOdAmoiIiqNE8XnGeFw8NrVKngpImHkNy776RACBLvP2oELvRjsgUVvo%2BJ%2FsnpqU8Tmnze6c45lKS4DsUDSxTcpEB79FfR0AyeYwXJzj3As7FxIIsLxxuIVtCynLk%2F%2FNLWYz5"
    var deviceValue = "android"
    var partnerValue = "pooq"
    var pooqzoneValue = "none"
    var regionValue = "kor"
    var drmValue = "wm"
    var targetAgeValue = "auto"
    var iapTypeValue = "purchase"

    constructor(builder: Builder) {
        this.credentialValue = builder.credentialValue
        this.deviceValue = builder.deviceValue
        this.partnerValue = builder.partnerValue
        this.pooqzoneValue = builder.pooqzoneValue
        this.regionValue = builder.regionValue
        this.drmValue = builder.drmValue
        this.targetAgeValue = builder.targetAgeValue
        this.iapTypeValue = builder.iapTypeValue
    }

    fun getNetworkParamsMap(): HashMap<String, String> {
        return HashMap<String, String>().apply {
            put(CREDENTIAL, credentialValue)
            put(DEVICE, deviceValue)
            put(PARTNER, partnerValue)
            put(POOQZONE, pooqzoneValue)
            put(REGION, regionValue)
            put(DRM, drmValue)
            put(TARGETAGE, targetAgeValue)
            put(IAPTYPE, iapTypeValue)
        }
    }

    class Builder {
        var credentialValue = "XRIextSiEr2lGvdexpQ88Xx0AzZMIiav%2BnGY6Wr0qbmuW9jIY%2ByGpQBWoexLBkFcn4uwKWovLFqtaz1%2FtczJxZqpaDHLUasE3cRhni7l8u11b2DsVhnVZf6qytMDOdAmoiIiqNE8XnGeFw8NrVKngpImHkNy776RACBLvP2oELvRjsgUVvo%2BJ%2FsnpqU8Tmnze6c45lKS4DsUDSxTcpEB79FfR0AyeYwXJzj3As7FxIIsLxxuIVtCynLk%2F%2FNLWYz5"
        var deviceValue = "android"
        var partnerValue = "pooq"
        var pooqzoneValue = "none"
        var regionValue = "kor"
        var drmValue = "wm"
        var targetAgeValue = "auto"
        var iapTypeValue = "purchase"

        fun setCredentialValue(value: String): Builder {
            this.credentialValue = value
            return this
        }

        fun setDeviceValue(value: String): Builder {
            this.deviceValue = value
            return this
        }

        fun setPartnerValue(value: String): Builder {
            this.partnerValue = value
            return this
        }

        fun setPooqzoneValue(value: String): Builder {
            this.pooqzoneValue = value
            return this
        }

        fun setRegionValue(value: String): Builder {
            this.regionValue = value
            return this
        }

        fun setDrmValue(value: String): Builder {
            this.drmValue = value
            return this
        }

        fun setTargetAgeValue(value: String): Builder {
            this.targetAgeValue = value
            return this
        }

        fun setIapTypeValue(value: String): Builder {
            this.iapTypeValue = value
            return this
        }

        fun build() : NetworkParam {
            return NetworkParam(this)
        }
    }
}