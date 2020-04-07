package com.android.githubusersapp.domain

import com.android.githubusersapp.domain.model.User
import io.reactivex.Single

interface UseCase {
    fun getUsers(query:String):Single<List<User>>
}