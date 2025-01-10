package com.aziz.githubretrofituser.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserDetailResponse(
    @field:SerializedName("avatar_url")
    val avatarUrl: String,

    @field:SerializedName("followers")
    val followers: Int,

    @field:SerializedName("following")
    val following: Int,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("public_repos")
    val publicRepos: Int,

    @field:SerializedName("login")
    val login: String,

    @field:SerializedName("email")
    val email: String,

    @field:SerializedName("html_url")
    val htmlUrl: String
) : Parcelable
