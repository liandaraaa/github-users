package com.android.githubusersapp.depth.module

import com.android.githubusersapp.BuildConfig
import com.android.githubusersapp.depth.service.OkhttpClientFactory
import io.reactivex.disposables.CompositeDisposable
import org.koin.core.qualifier.named
import org.koin.dsl.module

const val BASE_URL = "base_url"

val serviceModule = module {
    single { return@single OkhttpClientFactory.create()}
    single(named(BASE_URL)){BuildConfig.BASE_URL}
}

val rxModule = module{
    factory { CompositeDisposable() }
}