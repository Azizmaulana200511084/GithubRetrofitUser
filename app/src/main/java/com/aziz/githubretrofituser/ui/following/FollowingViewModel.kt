package com.aziz.githubretrofituser.ui.following

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aziz.githubretrofituser.adapter.ListUserAdapter
import com.aziz.githubretrofituser.databinding.FragmentFollowingBinding
import com.aziz.githubretrofituser.model.User
import com.aziz.githubretrofituser.ui.detail.UserDetailActivity

class FollowingFragment : Fragment() {
    private lateinit var binding: FragmentFollowingBinding
    private lateinit var username: String
    private val followingViewModel by viewModels<FollowingViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFollowingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        username = activity?.intent?.getStringExtra(UserDetailActivity.USERNAME).toString()

        followingViewModel.getFollowing(username)

        followingViewModel.isLoading.observe(requireActivity()) {
            showLoading(it)
        }

        followingViewModel.following.observe(requireActivity()) {
            showListFollower(it)
        }

        followingViewModel.isError.observe(requireActivity()) {
            if (it == true) {
                showError(it)
            }
        }
    }

    private fun showListFollower(user: List<User>) {
        val rvUser: RecyclerView = binding.rvFollowing
        val isEmptyUser = user.isEmpty()

        handlingEmptyUser(isEmptyUser)
        rvUser.layoutManager = LinearLayoutManager(requireActivity())
        val listHeroAdapter = ListUserAdapter(user)
        rvUser.adapter = listHeroAdapter

        listHeroAdapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: User) {
                if (binding.progressBar.visibility == View.GONE) {
                    showSelectedUser(data)
                }
            }
        })
    }

    private fun showSelectedUser(user: User) {
        val intent = Intent(requireActivity(), UserDetailActivity::class.java)
        intent.putExtra(UserDetailActivity.USERNAME, user.login)
        startActivity(intent)
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun handlingEmptyUser(isEmptyUser: Boolean) {
        if (isEmptyUser) {
            binding.emptyUser.clEmptyUser.visibility = View.VISIBLE
        } else {
            binding.emptyUser.clEmptyUser.visibility = View.GONE
        }
    }

    private fun showError(isError: Boolean) {
        if (isError) {
            binding.error.clError.visibility = View.VISIBLE
        } else {
            binding.error.clError.visibility = View.GONE
        }
    }
}