package com.android.githubusersapp.depth.module

import com.android.githubusersapp.BuildConfig
import com.android.githubusersapp.data.remote.Api
import com.android.githubusersapp.data.remote.ApiClient
import com.android.githubusersapp.depth.service.OkhttpClientFactory
import com.android.githubusersapp.depth.service.RetrofitService
import com.android.githubusersapp.domain.Interactor
import com.android.githubusersapp.domain.UseCase
import com.android.githubusersapp.repository.DataStore
import com.android.githubusersapp.repository.Repository
import com.android.githubusersapp.ui.viewmodel.UserViewModel
import com.google.gson.Gson
import io.reactivex.disposables.CompositeDisposable
import org.koin.android.viewmodel.dsl.viewModel
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

val utilityModule = module{
    single { Gson() }
}

val featureModule = module {
    single { RetrofitService.createReactiveService(
        ApiClient::class.java,
        get(),
        get(named(BASE_URL))
    ) }
    single { Api(get()) }
    single<Repository>{DataStore(get())}
    single<UseCase>{Interactor(get())}
    viewModel { UserViewModel(get(), get()) }
}