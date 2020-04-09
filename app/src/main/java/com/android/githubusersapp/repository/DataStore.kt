package com.android.githubusersapp.repository

import androidx.paging.DataSource
import com.android.githubusersapp.data.model.UserItem
import com.android.githubusersapp.data.remote.Api
import com.android.githubusersapp.depth.rx.singleApiError
import io.reactivex.Single

class DataStore (private val api:Api):Repository{
    override fun getUsers(query: String): Single<List<UserItem>> {
        return api.getUsers(query)
            .lift(singleApiError())
            .map { it.userItems }
    }

    override fun getUsers(query: String, limit: Int): Single<List<UserItem>> {
        return api.getUsers(query, limit)
            .lift(singleApiError())
            .map { it.userItems }
    }
}