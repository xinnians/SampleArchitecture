package com.example.repository.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SampleAPI : BaseAPI() {

    companion object {
        private val TAG = SampleAPI::class.simpleName
        private var sInstance: SampleAPI? = null
        private lateinit var mService: SampleService

        //一頁的listView顯示幾個，之後應由後台做設定
        private const val LISTVIEW_PAGECOUNT = 10
        private val SEARCH_JOIN_WORD = "and"

        fun getInstance(): SampleAPI? {
            if (sInstance == null) {
                synchronized(SampleAPI::class) {
                    if (sInstance == null) {
                        sInstance = SampleAPI()
                    }
                }
            }
            return sInstance
        }
    }

    init {
//        val url = "https://newsapi.org/v1/"
//        val url = "https://dev-inum-webapi.yjtech.tw/"
        val url = "https://qat-inum-webapi.yjtech.tw/"
        val client = getOkHttpClient()

        val retrofit = Retrofit.Builder()
            .baseUrl(url)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        mService = retrofit.create(SampleService::class.java)
    }

    fun getService(): SampleService = mService
    /*--------------------------------------------------------------------------------------------*/
    /* APIs */
}