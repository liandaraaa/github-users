package com.android.githubusersapp.datasource

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.android.githubusersapp.data.remote.Api
import com.android.githubusersapp.depth.rx.RxState
import com.android.githubusersapp.depth.rx.add
import com.android.githubusersapp.depth.rx.getErrorMessage
import com.android.githubusersapp.domain.UseCase
import com.android.githubusersapp.domain.model.User
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Action
import io.reactivex.schedulers.Schedulers
import java.util.*

class PagingDataSource<T>(val provider: PagingListProvider<T>) : PageKeyedDataSource<Int, T>() {

    companion object {
        const val PAGE_SIZE = 10
    }

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, T>
    ) {
        val list = provider.getList(0, params.requestedLoadSize)
        callback.onResult(list, null, 2)
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, T>) {
        val list = provider.getList(params.key, params.requestedLoadSize)
        callback.onResult(list, params.key + 1)
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, T>) {
        val list = provider.getList(params.key, params.requestedLoadSize)
        val nextIndex = if (params.key > 1) params.key - 1 else null
        callback.onResult(list, nextIndex)
    }
}

class PagingUserDataSource(
    val query: String,
    val useCase: UseCase,
    val compositeDisposable: CompositeDisposable,
    val initialRxState: MutableLiveData<RxState<List<User>>>,
    val paginationRxState: MutableLiveData<RxState<List<User>>>
) : PageKeyedDataSource<Int, User>() {

    var retryData: Completable? = null

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, User>
    ) {
        initialRxState.postValue(RxState.loading())
        useCase.getUsers(query, 1, 10).subscribe({ users ->
            doRerty()
            initialRxState.postValue(if (users.isEmpty()) RxState.empty() else RxState.success(users))
            callback.onResult(users, 1, 2)
            Log.d(
                "paging",
                "initial param $query, offset ${1} limit ${10} hasil ${users.size}"
            )
        }, { t: Throwable? ->
            doRerty(Action { loadInitial(params, callback) })
            initialRxState.postValue(RxState.error(t?.getErrorMessage()))
            Log.d(
                "paging",
                " error initial param $query, offset ${1} limit ${params.requestedLoadSize} hasil ${t?.message}"
            )
        }).add(compositeDisposable)
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, User>) {
        paginationRxState.postValue(RxState.loading())
        useCase.getUsers(query, params.key, params.requestedLoadSize).subscribe({ users ->
            doRerty()
            paginationRxState.postValue(
                if (users.isEmpty()) RxState.empty() else RxState.success(
                    users
                )
            )
            callback.onResult(users, params.key + 1)
            Log.d(
                "paging",
                "more param $query, offset ${params.key} ajacentPageKey ${params.key + 1}, limit ${params.requestedLoadSize} hasil ${users.size}"
            )
        }, { t: Throwable? ->
            doRerty(Action { loadAfter(params, callback) })
            paginationRxState.postValue(RxState.error(t?.getErrorMessage()))
            Log.d(
                "paging",
                " error more param ${params.key}, ${params.requestedLoadSize} hasil ${t?.message}"
            )
        }).add(compositeDisposable)
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, User>) {
    }

    fun retryLoadData() {
        retryData?.subscribeOn(Schedulers.io())?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe()?.add(compositeDisposable)
    }

    private fun doRerty(action: Action? = null) {
        if (action == null) {
            retryData = null
        } else {
            retryData = Completable.fromAction(action)
        }
    }
}