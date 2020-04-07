package com.android.githubusersapp.repository

import com.android.githubusersapp.data.model.UserItem
import io.reactivex.Single

interface Repository {
    fun getUsers(query:String):Single<List<UserItem>>
}