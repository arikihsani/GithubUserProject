package com.example.submission3.ui

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    @field:SerializedName("name")
    var name: String?,
    @field:SerializedName("login")
    var username: String?,
    @field:SerializedName("location")
    var location: String?,
    @field:SerializedName("public_repos")
    var repository: String?,
    @field:SerializedName("company")
    var company: String?,
    @field:SerializedName("followers")
    var followers: String?,
    @field:SerializedName("following")
    var following: String?,
    @field:SerializedName("avatar_url")
    var avatar_url: String?,
    @field:SerializedName("html_url")
    var html_url: String?,
    var isFavorite: Boolean?
) : Parcelable

