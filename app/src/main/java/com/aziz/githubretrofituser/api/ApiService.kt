package com.aziz.githubretrofituser.api

import com.aziz.githubretrofituser.model.User
import com.aziz.githubretrofituser.model.UserDetailResponse
import com.aziz.githubretrofituser.model.UserListResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("search/users")
    @Headers("Authorization: token ")
    fun searchUser(
        @Query("q") username: String
    ): Call<UserListResponse>

    @GET("users/{username}")
    @Headers("Authorization: token ")
    fun getUserDetail(@Path("username") username: String): Call<UserDetailResponse>

    @GET("users/{username}/followers")
    @Headers("Authorization: token ")
    fun getFollowers(@Path("username") username: String): Call<ArrayList<User>>

    @GET("users/{username}/following")
    @Headers("Authorization: token ")
    fun getFollowing(@Path("username") username: String): Call<ArrayList<User>>
}