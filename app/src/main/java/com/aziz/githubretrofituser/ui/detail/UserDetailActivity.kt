package com.aziz.githubretrofituser.ui.detail

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.aziz.githubretrofituser.R
import com.aziz.githubretrofituser.adapter.SectionsPagerAdapter
import com.aziz.githubretrofituser.database.FavoriteUser
import com.aziz.githubretrofituser.databinding.ActivityUserDetailBinding
import com.aziz.githubretrofituser.model.UserDetailResponse
import com.aziz.githubretrofituser.ui.setting.SettingActivity
import com.aziz.githubretrofituser.viewModel.ViewModelFactory
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class UserDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserDetailBinding
    private lateinit var username: String
    private var favoriteUser: FavoriteUser? = null
    private lateinit var userDetailViewModel: UserDetailViewModel
    private var findUserByLoginId: FavoriteUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupToolbar()

        username = intent.getStringExtra(USERNAME).toString()
        userDetailViewModel = obtainViewModel(this@UserDetailActivity)
        userDetailViewModel.getUserDetail(username)
        userDetailViewModel.getFavoriteUserByLoginId(username).observe(this) {
            if (it != null) {
                favoriteUser = it
                binding.favoriteFloatingButton.setImageDrawable(resources.getDrawable(R.drawable.ic_baseline_favorite_24))
            } else {
                favoriteUser = FavoriteUser()
                binding.favoriteFloatingButton.setImageDrawable(resources.getDrawable(R.drawable.ic_baseline_favorite_border_24))
            }
            findUserByLoginId = it
        }
        userDetailViewModel.isLoading.observe(this@UserDetailActivity) {
            showLoading(it)
        }
        userDetailViewModel.userDetail.observe(this@UserDetailActivity) {
            showUserDetail(it)
            buttonFavoriteHandler(it, userDetailViewModel)
        }
        userDetailViewModel.isError.observe(this@UserDetailActivity) {
            showError(it)
        }

        tabLayoutHandler()
    }

    private fun buttonFavoriteHandler(
        user: UserDetailResponse,
        userDetailViewModel: UserDetailViewModel
    ) {
        binding.favoriteFloatingButton.setOnClickListener {
            if (findUserByLoginId == null) {
                favoriteUser = FavoriteUser(
                    avatarUrl = user.avatarUrl,
                    name = user.name,
                    login = user.login,
                    email = user.email
                )
                userDetailViewModel.insert(favoriteUser as FavoriteUser)
                showSnackBar("User berhasil ditambahkan ke favorite!", it)
            } else {
                userDetailViewModel.delete(favoriteUser as FavoriteUser)
                showSnackBar("User berhasil dihapus dari favorite!", it)
            }
        }
    }

    private fun showSnackBar(message: String, view: View) {
        val snack = Snackbar.make(view, message, Snackbar.LENGTH_SHORT)
        snack.show()
    }

    private fun tabLayoutHandler() {
        val sectionsPagerAdapter = SectionsPagerAdapter(this@UserDetailActivity)
        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.tabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu_user_detail, menu)
        return true
    }

    private fun setupToolbar() {
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.displayOptions
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.shareButton -> {
                val sendIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(
                        Intent.EXTRA_TEXT,
                        "Link User : \n${userDetailViewModel.userDetail.value?.htmlUrl} "
                    )
                    type = "text/html"
                }
                val shareIntent = Intent.createChooser(sendIntent, "Link User")
                startActivity(shareIntent)
                return true
            }
            R.id.settingButton -> {
                val intent = Intent(this@UserDetailActivity, SettingActivity::class.java)
                startActivity(intent)
                return true
            }
            android.R.id.home -> {
                finish()
                return true
            }
            else -> return true
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
            binding.userDetail.clUserDetail.visibility = View.GONE
            binding.tabs.visibility = View.GONE
            binding.viewPager.visibility = View.GONE
            binding.viewDivider.visibility = View.GONE
            binding.favoriteFloatingButton.visibility = View.GONE
        } else {
            binding.progressBar.visibility = View.GONE
            binding.userDetail.clUserDetail.visibility = View.VISIBLE
            binding.tabs.visibility = View.VISIBLE
            binding.viewPager.visibility = View.VISIBLE
            binding.viewDivider.visibility = View.VISIBLE
            binding.favoriteFloatingButton.visibility = View.VISIBLE
        }
    }

    private fun showUserDetail(user: UserDetailResponse) {
        Glide.with(this@UserDetailActivity)
            .load(user.avatarUrl)
            .circleCrop()
            .into(binding.userDetail.cardUserInfo.ivAvatar)
        binding.userDetail.cardUserInfo.tvName.text = user.name
        binding.userDetail.cardUserInfo.tvUsername.text = user.login
        binding.userDetail.cardUserDetail.tvFollowers.text = StringBuilder()
            .append(user.followers.toString())
            .append(this@UserDetailActivity.getString(R.string.followers))
        binding.userDetail.cardUserDetail.tvFollowing.text = StringBuilder()
            .append(user.following.toString())
            .append(this@UserDetailActivity.getString(R.string.following))
    }

    private fun showError(isError: Boolean) {
        if (isError) {
            binding.error.clError.visibility = View.VISIBLE
            binding.userDetail.clUserDetail.visibility = View.GONE
            binding.tabs.visibility = View.GONE
            binding.viewPager.visibility = View.GONE
            binding.viewDivider.visibility = View.GONE
        } else {
            binding.error.clError.visibility = View.GONE
            binding.userDetail.clUserDetail.visibility = View.VISIBLE
            binding.tabs.visibility = View.VISIBLE
            binding.viewPager.visibility = View.VISIBLE
            binding.viewDivider.visibility = View.VISIBLE
        }
    }

    private fun obtainViewModel(activity: AppCompatActivity): UserDetailViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[UserDetailViewModel::class.java]
    }

    companion object {
        const val USERNAME = "username"
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
    }
}