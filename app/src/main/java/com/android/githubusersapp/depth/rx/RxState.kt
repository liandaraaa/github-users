package com.android.githubusersapp.depth.rx

sealed class RxState<T>{
    class Loading<T> : RxState<T>()
    class Empty<T> : RxState<T>()
    class Success<T>(val data:T) : RxState<T>()
    class Error<T>(val message:String?):RxState<T>()

    companion object{
        fun <T> loading():RxState<T> = Loading()
        fun <T> empty():RxState<T> = Empty()
        fun <T> success(data:T):RxState<T> = Success(data)
        fun <T> error(message: String?):RxState<T> = RxState.Error(message)
    }
}