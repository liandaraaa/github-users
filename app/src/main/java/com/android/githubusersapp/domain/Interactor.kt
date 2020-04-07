package com.android.githubusersapp.domain

import com.android.githubusersapp.domain.model.User
import com.android.githubusersapp.repository.DataStore
import io.reactivex.Single

class Interactor (val dataStore: DataStore):UseCase{
    override fun getUsers(query: String): Single<List<User>> {
        return dataStore.getUsers(query)
            .map { it.map {usertItem->
                User(
                    name = usertItem.login.orEmpty(),
                    avatar = usertItem.avatarUrl.orEmpty()
                )
            } }
    }

}