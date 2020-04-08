package com.android.githubusersapp.data.model


import com.google.gson.annotations.SerializedName

data class UserResponse(
    @SerializedName("incomplete_results")
    val incompleteResults: Boolean,
    @SerializedName("items")
    val userItems: List<UserItem>?,
    @SerializedName("total_count")
    val totalCount: Int?
)