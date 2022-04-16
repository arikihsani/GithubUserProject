package com.example.submission3.api

import com.example.submission3.ui.User
import com.example.submission3.ui.UserResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("search/users")
    fun getListUsers(@Query("q") q: String): Call<UserResponse>

    @GET("users/{username}")
    fun getUserDetail(@Path("username") username: String?): Call<User>

    @GET("users/{username}/followers")
    fun getFollowers(@Path("username") username: String?): Call<ArrayList<User>>

    @GET("users/{username}/following")
    fun getFollowing(@Path("username") username: String?): Call<ArrayList<User>>

    @GET("users")
    fun getInitialListUsers(): Call<ArrayList<User>>
}
