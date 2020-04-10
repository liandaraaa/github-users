package com.android.githubusersapp.datasource

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.android.githubusersapp.depth.rx.RxState
import com.android.githubusersapp.domain.UseCase
import com.android.githubusersapp.domain.model.User
import io.reactivex.disposables.CompositeDisposable
import java.util.*

class PagingUserDataSourceFactory (
    val query:String,
    val useCase: UseCase,
    val compositeDisposable: CompositeDisposable,
    val initialRxState:MutableLiveData<RxState<List<User>>>,
    val paginationRxState:MutableLiveData<RxState<List<User>>>
): DataSource.Factory<Int, User>() {

    val userDataSource = PagingUserDataSource(query,useCase,compositeDisposable,initialRxState,paginationRxState)
    val userDataSourceLiveData = MutableLiveData<PagingUserDataSource>()

    override fun create(): DataSource<Int, User> {
        userDataSourceLiveData.postValue(userDataSource)
        return userDataSource
    }
}
