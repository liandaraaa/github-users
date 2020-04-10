package com.android.githubusersapp.datasource

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.android.githubusersapp.depth.rx.RxState
import com.android.githubusersapp.domain.UseCase
import com.android.githubusersapp.domain.model.User
import io.reactivex.disposables.CompositeDisposable

class PagingUserDataSourceFactory(
    val query: String,
    private val useCase: UseCase,
    private val compositeDisposable: CompositeDisposable,
    private val initialRxState: MutableLiveData<RxState<List<User>>>,
    private val paginationRxState: MutableLiveData<RxState<List<User>>>
) : DataSource.Factory<Int, User>() {

    val userDataSource =
        PagingUserDataSource(query, useCase, compositeDisposable, initialRxState, paginationRxState)
    private val userDataSourceLiveData = MutableLiveData<PagingUserDataSource>()

    override fun create(): DataSource<Int, User> {
        userDataSourceLiveData.postValue(userDataSource)
        return userDataSource
    }
}
