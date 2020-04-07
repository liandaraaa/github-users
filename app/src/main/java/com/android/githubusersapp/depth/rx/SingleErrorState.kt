package com.android.githubusersapp.depth.rx

import com.android.githubusersapp.depth.service.model.ErrorResponse
import com.android.githubusersapp.depth.service.model.ResponseException
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import io.reactivex.SingleObserver
import io.reactivex.SingleOperator
import io.reactivex.disposables.Disposable
import retrofit2.Response
import java.io.IOException
import java.lang.RuntimeException

class SingleErrorState<T>: SingleOperator<T, Response<T>>{
    override fun apply(observer: SingleObserver<in T>): SingleObserver<in Response<T>> {
        return object :SingleObserver<Response<T>>{
            override fun onSuccess(response: Response<T>) {
                if (!response.isSuccessful){
                    try {
                        var error: ErrorResponse? = null
                        try {
                            error = Gson().fromJson(
                                response.errorBody()!!.string(),
                                ErrorResponse::class.java
                            )
                            error?.let { observer.onError(ResponseException(it)) }
                        } catch (e: JsonSyntaxException) {
                            error = ErrorResponse("Error Not Found")
                            observer.onError(ResponseException(error))
                        }
                    }catch (e:IOException){
                        observer.onError(e)
                    }
                }else{
                    response.body()?.let { observer.onSuccess(it) }
                }
            }

            override fun onSubscribe(disposable: Disposable) {
                observer.onSubscribe(disposable)
            }

            override fun onError(error: Throwable) {
                observer.onError(error)
            }

        }
    }
}