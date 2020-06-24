package com.example.repository.api

import android.content.Context
import android.os.Build
import android.util.Log
import com.example.repository.JsonUtil
import com.example.repository.LoggingInterceptor
import okhttp3.ConnectionPool
import okhttp3.ConnectionSpec
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.lang.Exception
import java.lang.ref.WeakReference
import java.util.concurrent.TimeUnit

abstract class BaseAPI {

    companion object {
        private val TAG = BaseAPI::class.simpleName
        private const val MAX_IDLE_CONNECTIONS = 8
        private const val KEEP_ALIVE_DURATION = 300L
        private const val CONNECTION_TIMEOUT = 15L
        private const val READ_TIMEOUT = 15L
        private const val WRITE_TIMEOUT = 15L

        private lateinit var sContextRef: WeakReference<Context>
        private var mOkHttpClient: OkHttpClient? = null

        fun init(context: Context) {
            sContextRef = WeakReference(context)
        }

        private fun createClient(): OkHttpClient {
            var context = sContextRef.get()
            logInterceptor.apply { logInterceptor.level = HttpLoggingInterceptor.Level.BODY }
            val connectionPool = ConnectionPool(MAX_IDLE_CONNECTIONS, KEEP_ALIVE_DURATION, TimeUnit.SECONDS)
            val builder = OkHttpClient.Builder()
            builder.connectionPool(connectionPool)
                .connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .addInterceptor(LoggingInterceptor())
                .addInterceptor(logInterceptor)

            return enableTLS120nPreLollipop(builder).build()
        }

        fun getOkHttpClient(): OkHttpClient? {
            if (mOkHttpClient == null) {
                mOkHttpClient = createClient()
            }
            return mOkHttpClient
        }

        private fun enableTLS120nPreLollipop(builder: OkHttpClient.Builder): OkHttpClient.Builder {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN && Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT_WATCH) {
                try {
                    builder.connectionSpecs(arrayListOf(ConnectionSpec.MODERN_TLS, ConnectionSpec.COMPATIBLE_TLS, ConnectionSpec.CLEARTEXT))
                } catch (e: Exception) {
                    Log.e(TAG, "[enableTLS12OnPreLollipop] Error while setting TLS 1.2 ${e.message}")
                }
            }
            return builder
        }

        val logInterceptor = HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
            private val mMessage = StringBuilder()
            override fun log(message: String) {
                var message = message
                if (message.startsWith("--> POST") || message.startsWith("--> GET")) {
                    mMessage.setLength(0)
                }
                if (message.startsWith("{") && message.endsWith("}") || message.startsWith("[") && message.endsWith(
                        "]"
                    )) {
                    message = JsonUtil.formatJson(
                        JsonUtil.decodeUnicode(message)
                    )
                }
                mMessage.append(message + "\n")
                if (message.startsWith("<-- END HTTP")) {
                    Log.d("[logInterceptor]",mMessage.toString())
                }
            }
        })
    }
}