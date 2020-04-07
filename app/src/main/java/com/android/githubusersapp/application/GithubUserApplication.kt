package com.android.githubusersapp.application

import android.app.Application
import com.android.githubusersapp.depth.koin.KoinContext
import com.android.githubusersapp.depth.module.rxModule
import com.android.githubusersapp.depth.module.serviceModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class GithubUserApplication :Application(){
    override fun onCreate() {
        super.onCreate()

        KoinContext.initialize(applicationContext)

        startKoin {
            androidContext(this@GithubUserApplication)
            modules(
                listOf(
                    serviceModule,
                    rxModule
                )
            )
        }

    }
}