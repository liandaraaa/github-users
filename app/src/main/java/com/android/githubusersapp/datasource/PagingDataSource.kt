package com.android.githubusersapp.datasource

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
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

class PagingUserDataSource(
    val query: String,
    private val useCase: UseCase,
    private val compositeDisposable: CompositeDisposable,
    val initialRxState: MutableLiveData<RxState<List<User>>>,
    val paginationRxState: MutableLiveData<RxState<List<User>>>
) : PageKeyedDataSource<Int, User>() {

    companion object{
        const val INITIAL_OFFSET = 1
        const val LIMIT = 10
    }

    var retryData: Completable? = null

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, User>
    ) {
        initialRxState.postValue(RxState.loading())
        useCase.getUsers(query, INITIAL_OFFSET, LIMIT).subscribe({ users ->
            setRetry()
            initialRxState.postValue(if (users.isEmpty()) RxState.empty() else RxState.success(users))
            paginationRxState.postValue(if (users.isEmpty()) RxState.empty() else RxState.success(users))
            callback.onResult(users, INITIAL_OFFSET, INITIAL_OFFSET + 1)
        }, { t: Throwable? ->
            setRetry(Action { loadInitial(params, callback) })
            initialRxState.postValue(RxState.error(t?.getErrorMessage()))
        }).add(compositeDisposable)
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, User>) {
        paginationRxState.postValue(RxState.loading())
        useCase.getUsers(query, params.key, params.requestedLoadSize).subscribe({ users ->
            setRetry()
            paginationRxState.postValue(
                if (users.isEmpty()) RxState.empty() else RxState.success(
                    users
                )
            )
            callback.onResult(users, params.key + 1)
        }, { t: Throwable? ->
            setRetry(Action { loadAfter(params, callback) })
            paginationRxState.postValue(RxState.error(t?.getErrorMessage()))
        }).add(compositeDisposable)
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, User>) {
    }

    fun doRetry() {
        retryData?.subscribeOn(Schedulers.io())?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe()?.add(compositeDisposable)
    }

    private fun setRetry(action: Action? = null) {
        retryData = if (action == null) {
            null
        } else {
            Completable.fromAction(action)
        }
    }
}