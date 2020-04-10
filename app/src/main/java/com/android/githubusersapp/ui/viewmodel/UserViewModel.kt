package com.android.githubusersapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.android.githubusersapp.datasource.PagingUserDataSource.Companion.LIMIT
import com.android.githubusersapp.datasource.PagingUserDataSourceFactory
import com.android.githubusersapp.depth.rx.RxState
import com.android.githubusersapp.domain.UseCase
import com.android.githubusersapp.domain.model.User
import com.android.githubusersapp.utils.initializedPagedListBuilder
import io.reactivex.disposables.CompositeDisposable

class UserViewModel (
    private val useCase: UseCase,
    private val disposable: CompositeDisposable
):ViewModel(){

    private lateinit var sourceFactory:PagingUserDataSourceFactory

    fun getUsers(query: String):LiveData<PagedList<User>>{
        sourceFactory = PagingUserDataSourceFactory(query,useCase,disposable, MutableLiveData(), MutableLiveData())
        return initializedPagedListBuilder(sourceFactory, LIMIT).build()
    }

    fun doRetry(){
        sourceFactory.userDataSource.doRetry()
    }

    fun getPaginationRxState():MutableLiveData<RxState<List<User>>>{
        return sourceFactory.userDataSource.paginationRxState
    }

    fun getInitialRxState():MutableLiveData<RxState<List<User>>>{
        return sourceFactory.userDataSource.initialRxState
    }

    override fun onCleared() {
        super.onCleared()
        if (!disposable.isDisposed){
            disposable.dispose()
        }
    }
}