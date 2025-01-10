package com.aziz.githubretrofituser.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.aziz.githubretrofituser.database.FavoriteUser
import com.aziz.githubretrofituser.database.FavoriteUserDao
import com.aziz.githubretrofituser.database.FavoriteUserDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FavoriteUserRepository(application: Application) {
    private val mFavoriteUserDao: FavoriteUserDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = FavoriteUserDatabase.getDatabase(application)
        mFavoriteUserDao = db.noteDao()
    }

    fun getAllFavoriteUser(): LiveData<List<FavoriteUser>> = mFavoriteUserDao.getAllFavoriteUser()

    fun insert(favoriteUser: FavoriteUser) {
        executorService.execute { mFavoriteUserDao.insert(favoriteUser) }
    }

    fun delete(favoriteUser: FavoriteUser) {
        executorService.execute { mFavoriteUserDao.delete(favoriteUser) }
    }

    fun getFavoriteUserByLoginId(login: String): LiveData<FavoriteUser> =
        mFavoriteUserDao.getFavoriteUserByLoginId(login)
}