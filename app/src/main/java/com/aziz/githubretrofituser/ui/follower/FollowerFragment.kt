package com.aziz.githubretrofituser.ui.follower

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
import com.aziz.githubretrofituser.databinding.FragmentFollowerBinding
import com.aziz.githubretrofituser.model.User
import com.aziz.githubretrofituser.ui.detail.UserDetailActivity

class FollowerFragment : Fragment() {
    private val followerViewModel by viewModels<FollowerViewModel>()
    private var _binding: FragmentFollowerBinding? = null
    private val binding get() = _binding!!
    private lateinit var username: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFollowerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        username = activity?.intent?.getStringExtra(UserDetailActivity.USERNAME).toString()
        followerViewModel.getFollowers(username)
        followerViewModel.isLoading.observe(viewLifecycleOwner) { showLoading(it) }
        followerViewModel.followers.observe(viewLifecycleOwner) { showListFollower(it) }
        followerViewModel.isError.observe(viewLifecycleOwner) { showError(it) }
    }

    private fun showListFollower(user: List<User>) {
        val rvUser: RecyclerView = binding.rvFollowers
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
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun handlingEmptyUser(isEmptyUser: Boolean) {
        binding.emptyUser.clEmptyUser.visibility = if (isEmptyUser) View.VISIBLE else View.GONE
    }

    private fun showError(isError: Boolean) {
        binding.error.clError.visibility = if (isError) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
