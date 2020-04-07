package com.android.githubusersapp.depth.rx

import com.android.githubusersapp.depth.service.model.ResponseException
import com.google.gson.JsonSyntaxException
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import java.io.IOException
import java.net.SocketTimeoutException

fun Disposable.add(disposable: CompositeDisposable){
    disposable.add(this)
}

fun Throwable.getErrorMessage(throwable:Throwable):String{
    return when(this){
        is ResponseException -> throwable.message ?: "Error Not Found"
        is SocketTimeoutException -> "Connection Timeout"
        is IOException -> "Connection IO Exception"
        is JsonSyntaxException -> "JSON Exception"
        else -> "Unknown Error"

    }
}

fun <T> singleApiError():SingleErrorState<T>{
    return SingleErrorState()
}