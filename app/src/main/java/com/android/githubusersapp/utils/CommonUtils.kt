package com.android.githubusersapp.utils

import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.android.githubusersapp.datasource.PagingDataSource
import com.android.githubusersapp.datasource.PagingListProvider

fun <K,T> initializedPagedListBuilder(dataSource: DataSource.Factory<K,T>): LivePagedListBuilder<K, T> {
    val pagedListConfig = PagedList.Config.Builder()
        .setEnablePlaceholders(true)
        .setPageSize(PagingDataSource.PAGE_SIZE)
        .build()

    return LivePagedListBuilder<K, T>(dataSource, pagedListConfig)
}

