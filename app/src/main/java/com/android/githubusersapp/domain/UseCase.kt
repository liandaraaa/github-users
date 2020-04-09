package com.android.githubusersapp.domain

import androidx.paging.DataSource
import androidx.paging.PagedList
import com.android.githubusersapp.domain.model.User
import io.reactivex.Single

interface UseCase {
    fun getUsers(query:String):Single<List<User>>
    fun getUsers(query:String, limit:Int):Single<List<User>>
}