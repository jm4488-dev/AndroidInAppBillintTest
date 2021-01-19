package com.jm4488.retrofitservice

import android.os.Build
import android.util.Log
import com.jm4488.retrofitservice.Base.BaseApiInstance
import com.jm4488.retrofitservice.Factory.NullOnEmptyConverterFactory
import com.jm4488.retrofitservice.Factory.TLSSocketFactory
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.IOException
import java.util.*
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext

class RestfulService {
    private object SingletonHolder {
        val INSTANCE = RestfulService()
    }

    companion object {
        private const val HTTP_READ_TIMEOUT = 10000
        private const val HTTP_CONNECT_TIMEOUT = 4000
        private var httpClient: OkHttpClient? = null
        private var httpClientWithParamAndCredential: OkHttpClient? = null

        private lateinit var retrofitBuilder: Retrofit.Builder

        @JvmStatic
        fun getInstance(): RestfulService {
            return SingletonHolder.INSTANCE
        }
    }

    fun initRetrofitBuilder(url: String) {
        retrofitBuilder = Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(NullOnEmptyConverterFactory.create())
            .addConverterFactory(ScalarsConverterFactory.create()) // post 날릴려면 필요!
            .addConverterFactory(GsonConverterFactory.create())
    }

    fun <T> getApiInstance(params: HashMap<String, String>, serviceClass: Class<T>): T {
        return retrofitBuilder.client(provideClient(params)).build().create(serviceClass)
    }

    private fun provideClient(params: HashMap<String, String>): OkHttpClient? {
        return if (httpClientWithParamAndCredential == null) {
            createHttpBuilder(params).build()
        } else {
            httpClientWithParamAndCredential
        }
    }

    private fun createHttpBuilder(params: HashMap<String, String>): OkHttpClient.Builder {
        val interceptor: Interceptor = RequestInterceptor(params)
        val httpClientBuilder = OkHttpClient.Builder()
        httpClientBuilder.addInterceptor(interceptor)
            .connectTimeout(HTTP_CONNECT_TIMEOUT.toLong(), TimeUnit.SECONDS)
            .readTimeout(HTTP_READ_TIMEOUT.toLong(), TimeUnit.SECONDS)
        val logInterceptor = HttpLoggingInterceptor()
        logInterceptor.setLevel(if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE)
        if (BuildConfig.DEBUG) {
            httpClientBuilder.addInterceptor(logInterceptor)
        }
        return enableTlsOnPreLollipop(httpClientBuilder)
    }

    fun enableTlsOnPreLollipop(builder: OkHttpClient.Builder): OkHttpClient.Builder {
        if (Build.VERSION.SDK_INT in 16..21) {
            try {
                val sc = SSLContext.getInstance("TLSv1.2")
                sc.init(null, null, null)
                builder.sslSocketFactory(TLSSocketFactory(sc.socketFactory))
                val cs = ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                    .tlsVersions(TlsVersion.TLS_1_2)
                    .build()
                val specs: MutableList<ConnectionSpec> = ArrayList()
                specs.add(cs)
                specs.add(ConnectionSpec.COMPATIBLE_TLS)
                specs.add(ConnectionSpec.CLEARTEXT)
                builder.connectionSpecs(specs)
            } catch (exc: Exception) {
                Log.e("OkHttpTLSCompat", "Error while setting TLS 1.2", exc)
            }
        }
        return builder
    }

    internal class RequestInterceptor : Interceptor {
        private var queryParamsMap = HashMap<String, String>()

        constructor(params: HashMap<String, String>) {
            this.queryParamsMap = params
        }

        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response {
            // ??? 생각해보자....
            var request = chain.request()
            request = request.newBuilder().build()

            val urlBuilder = request.url().newBuilder()
            urlBuilder.apply {
                for (paramKey in queryParamsMap.keys) {
                    addQueryParameter(paramKey, queryParamsMap.get(paramKey))
                }
            }
            val url = urlBuilder.build()
            request = request.newBuilder().url(url).build()
            return chain.proceed(request)
        }
    }
}