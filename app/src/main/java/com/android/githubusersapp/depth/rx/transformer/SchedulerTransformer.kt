package com.android.githubusersapp.depth.rx.transformer

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

fun <T> singleScheduler(
    subscriber: Scheduler = Schedulers.io(),
    observer: Scheduler = AndroidSchedulers.mainThread()
): SingleSchedulerTransformer<T> {
    return SingleSchedulerTransformer(subscriber, observer)
}