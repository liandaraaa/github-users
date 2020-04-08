package com.android.githubusersapp.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.githubusersapp.depth.rx.RxState
import com.android.githubusersapp.depth.rx.add
import com.android.githubusersapp.depth.rx.getErrorMessage
import com.android.githubusersapp.depth.rx.transformer.singleScheduler
import com.android.githubusersapp.domain.UseCase
import com.android.githubusersapp.domain.model.User
import io.reactivex.disposables.CompositeDisposable

class UserViewModel (
    val useCase: UseCase,
    val disposable: CompositeDisposable
):ViewModel(){

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

    override fun onCleared() {
        super.onCleared()
        if (!disposable.isDisposed){
            disposable.dispose()
        }
    }
}