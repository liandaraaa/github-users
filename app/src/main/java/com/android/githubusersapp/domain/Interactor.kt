package com.android.githubusersapp.domain

import com.android.githubusersapp.domain.model.User
import com.android.githubusersapp.repository.Repository
import io.reactivex.Single

class Interactor (val repository: Repository):UseCase{
    override fun getUsers(query: String, offset:Int, limit: Int): Single<List<User>> {
        return repository.getUsers(query, offset, limit)
            .map { it.map {usertItem->
                User(
                    name = usertItem.login.orEmpty(),
                    avatar = usertItem.avatarUrl.orEmpty(),
                    id = usertItem.id ?: 0
                )
            } }
    }


}