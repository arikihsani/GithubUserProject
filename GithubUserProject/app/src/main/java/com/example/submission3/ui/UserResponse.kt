package com.example.submission3.ui

import com.google.gson.annotations.SerializedName

data class UserResponse(
    @field:SerializedName("total_count")
    val total_count: Int,

    @field:SerializedName("incomplete_results")
    val incomplete_results: Boolean,

    @field:SerializedName("items")
    val items: List<User>
)


