package com.android.githubusersapp.depth.service

import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

object OkhttpClientFactory {
    const val NETWORK_CALL_TIMEOUT = 60

    fun create(): OkHttpClient {
        val builder = OkHttpClient.Builder()
            .readTimeout(120, TimeUnit.SECONDS)
            .connectTimeout(120, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)

        val dispatcher = Dispatcher()
        dispatcher.maxRequests = NETWORK_CALL_TIMEOUT
        builder.dispatcher(dispatcher)
        return builder.build()
    }
}