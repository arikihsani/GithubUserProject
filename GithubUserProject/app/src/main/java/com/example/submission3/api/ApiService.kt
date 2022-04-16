package com.example.submission3.api

import com.example.submission3.ui.User
import com.example.submission3.ui.UserResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("search/users")
    @Headers("Authorization: token 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")
    fun getListUsers(@Query("q") q: String): Call<UserResponse>

    @GET("users/{username}")
    @Headers("Authorization: token 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")
    fun getUserDetail(@Path("username") username: String?): Call<User>

    @GET("users/{username}/followers")
    @Headers("Authorization: token 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")
    fun getFollowers(@Path("username") username: String?): Call<ArrayList<User>>

    @GET("users/{username}/following")
    @Headers("Authorization: token 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")
    fun getFollowing(@Path("username") username: String?): Call<ArrayList<User>>

    @GET("users")
    @Headers("Authorization: token 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")
    fun getInitialListUsers(): Call<ArrayList<User>>
}