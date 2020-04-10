package com.android.githubusersapp.data.remote

import com.android.githubusersapp.data.model.UserResponse
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiClient {
    @GET("search/users")
    fun getUsers(@Query("q") query:String,
                 @Query("page") offset:Int,
                 @Query("per_page") limit:Int):Single<Response<UserResponse>>
}