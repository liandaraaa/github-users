package com.android.githubusersapp.repository

import androidx.paging.DataSource
import com.android.githubusersapp.data.model.UserItem
import io.reactivex.Single

interface Repository {
    fun getUsers(query:String):Single<List<UserItem>>
    fun getUsers(query:String, offset:Int, limit:Int):Single<List<UserItem>>
}