package com.android.githubusersapp.data.remote

import com.android.githubusersapp.data.model.UserResponse
import io.reactivex.Single
import retrofit2.Response

class Api(private val apiClient: ApiClient):ApiClient{
    override fun getUsers(query: String): Single<Response<UserResponse>> {
        return apiClient.getUsers(query)
    }
}