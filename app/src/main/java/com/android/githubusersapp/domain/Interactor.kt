package com.android.githubusersapp.domain

import com.android.githubusersapp.domain.model.User
import com.android.githubusersapp.repository.Repository
import io.reactivex.Single

class Interactor (val repository: Repository):UseCase{
    override fun getUsers(query: String): Single<List<User>> {
        return repository.getUsers(query)
            .map { it.map {usertItem->
                User(
                    name = usertItem.login.orEmpty(),
                    avatar = usertItem.avatarUrl.orEmpty()
                )
            } }
    }

}