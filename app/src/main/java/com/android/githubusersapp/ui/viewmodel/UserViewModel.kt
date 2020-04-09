package com.android.githubusersapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.android.githubusersapp.datasource.PagingUserDataSourceFactory
import com.android.githubusersapp.depth.rx.RxState
import com.android.githubusersapp.depth.rx.add
import com.android.githubusersapp.depth.rx.getErrorMessage
import com.android.githubusersapp.depth.rx.transformer.singleScheduler
import com.android.githubusersapp.domain.UseCase
import com.android.githubusersapp.domain.model.User
import com.android.githubusersapp.utils.initializedPagedListBuilder
import io.reactivex.disposables.CompositeDisposable
import java.util.*

class UserViewModel (
    val useCase: UseCase,
    val disposable: CompositeDisposable
):ViewModel(){

    private lateinit var sourceFactory:PagingUserDataSourceFactory

    fun getUsers(query:String):MutableLiveData<RxState<List<User>>>{
        val users = MutableLiveData<RxState<List<User>>>()
        users.value = RxState.loading()
        useCase.getUsers(query)
            .compose(singleScheduler())
            .subscribe({
                users.value = if(it.isEmpty()) RxState.empty() else RxState.success(it)
            },{
                users.value = RxState.error(it.getErrorMessage())
            })
            .add(disposable)
        return users
    }

    fun getPagingUsers(query: String):LiveData<PagedList<User>>{
        sourceFactory = PagingUserDataSourceFactory(query,useCase,disposable, MutableLiveData(), MutableLiveData())
        return initializedPagedListBuilder(sourceFactory).build()
    }

    fun rertry(){
        sourceFactory.userDataSource.retryLoadData()
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