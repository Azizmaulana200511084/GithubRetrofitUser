package com.aziz.githubretrofituser.ui.favorites

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.aziz.githubretrofituser.R
import com.aziz.githubretrofituser.adapter.FavoriteUserAdapter
import com.aziz.githubretrofituser.databinding.ActivityFavoritesBinding
import com.aziz.githubretrofituser.ui.setting.SettingActivity
import com.aziz.githubretrofituser.viewModel.ViewModelFactory

class FavoritesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoritesBinding
    private lateinit var adapter: FavoriteUserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFavoritesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()

        val favoriteUserViewModel = obtainViewModel(this@FavoritesActivity)
        favoriteUserViewModel.getAllFavoriteUser().observe(this) { favoriteUserList ->
            if (favoriteUserList != null && favoriteUserList.isNotEmpty()) {
                adapter.setFavoriteUserList(favoriteUserList)
                showFavoriteUserList()
            } else {
                showEmptyFavorite()
            }
        }

        adapter = FavoriteUserAdapter()
    }

    private fun showEmptyFavorite() {
        binding.emptyUser.clEmptyUser.visibility = View.VISIBLE
        binding.rvFavoritesUsers.visibility = View.GONE
    }

    private fun showFavoriteUserList() {
        binding.rvFavoritesUsers.layoutManager = LinearLayoutManager(this@FavoritesActivity)
        binding.rvFavoritesUsers.setHasFixedSize(true)
        binding.rvFavoritesUsers.adapter = adapter
        binding.rvFavoritesUsers.visibility = View.VISIBLE
        binding.emptyUser.clEmptyUser.visibility = View.GONE
    }

    private fun obtainViewModel(activity: AppCompatActivity): FavoritesViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[FavoritesViewModel::class.java]
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu_favorite_user, menu)

        return true
    }

    private fun setupToolbar() {
        title = resources.getString(R.string.title_favorite_user)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.settingButton -> {
                val intent = Intent(this@FavoritesActivity, SettingActivity::class.java)
                startActivity(intent)
                return true
            }
            android.R.id.home -> {
                finish()
                true
            }
            else -> true
        }
    }
}