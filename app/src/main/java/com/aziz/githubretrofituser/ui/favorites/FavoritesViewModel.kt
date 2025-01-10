package com.aziz.githubretrofituser.ui.favorites

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.aziz.githubretrofituser.database.FavoriteUser
import com.aziz.githubretrofituser.repository.FavoriteUserRepository

class FavoritesViewModel(application: Application) : ViewModel() {
    private val mFavoriteUserRepository: FavoriteUserRepository =
        FavoriteUserRepository(application)

    fun getAllFavoriteUser(): LiveData<List<FavoriteUser>> =
        mFavoriteUserRepository.getAllFavoriteUser()
}