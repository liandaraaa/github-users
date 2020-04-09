package com.android.githubusersapp.datasource

class PagingListProvider<T>(val list:List<T>){
    fun getList(page:Int, pageSize:Int):List<T>{
        val initialIndex = page*pageSize
        val finalIndex = initialIndex + pageSize
        return list.subList(initialIndex, finalIndex)
    }
}