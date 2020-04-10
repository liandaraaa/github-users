package com.android.githubusersapp.utils

import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList

fun <K,T> initializedPagedListBuilder(dataSource: DataSource.Factory<K,T>, pageSize:Int): LivePagedListBuilder<K, T> {
    val pagedListConfig = PagedList.Config.Builder()
        .setEnablePlaceholders(true)
        .setPageSize(pageSize)
        .build()

    return LivePagedListBuilder<K, T>(dataSource, pagedListConfig)
}

