package com.android.githubusersapp.depth.koin

import android.annotation.SuppressLint
import android.content.Context

@SuppressLint("StaticFieldLeak")
object KoinContext {
    private var context:Context? = null

    private fun init(context: Context){
        this.context = context
    }

    private fun getContext():Context{
        checkNotNull(context){"first init"}
        return context!!
    }

    @JvmStatic
    fun initialize(context: Context){
        init(context)
    }

    @JvmStatic
    fun get():Context{
        return getContext()
    }
}