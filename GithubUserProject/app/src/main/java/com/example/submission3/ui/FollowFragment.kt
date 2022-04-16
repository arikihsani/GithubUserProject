package com.example.submission3.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.submission3.ui.UserDetail.Companion.EXTRA_USER
import com.example.submission3.databinding.FragmentFollowerBinding


class FollowFragment : Fragment() {

    private lateinit var binding: FragmentFollowerBinding

    companion object {
        const val EXTRA_FOLLOW = "extra_follow"

        @JvmStatic
        fun newInstance(index: Int) =
            FollowFragment().apply {
                arguments = Bundle().apply {
                    putInt(EXTRA_FOLLOW, index)
                }
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentFollowerBinding.inflate(layoutInflater)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val followViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            FollowViewModel::class.java
        )

        val check = arguments?.getInt(EXTRA_FOLLOW, 0)
        val user = this.activity?.intent?.getParcelableExtra<User>(EXTRA_USER) as User
        if (check == 0) {
            followViewModel.loadFollower(user.username as String)
        } else {
            followViewModel.loadFollowing(user.username as String)
        }

        followViewModel.follow.observe(viewLifecycleOwner, { follow ->
            if (follow != null) {
                setFollowersData(follow)
            }
        })
        followViewModel.isLoading.observe(viewLifecycleOwner, {
            showLoading(it)
        })
    }

    private fun setFollowersData(listFromApiUser: ArrayList<User>) {
        binding.rvFollower.layoutManager = LinearLayoutManager(activity)
        val adapter = RecyclerAdapter(listFromApiUser)
        binding.rvFollower.adapter = adapter
        adapter.setOnItemClickCallback(object : RecyclerAdapter.OnItemClickCallback {
            override fun onItemClicked(data: User) {
                showSelectedUser(data)
                val moveWithObjectIntent = Intent(activity, UserDetail::class.java)
                moveWithObjectIntent.putExtra(EXTRA_USER, data)
                startActivity(moveWithObjectIntent)
            }
        })
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showSelectedUser(user: User) {
        Toast.makeText(activity, "Anda memilih " + user.username, Toast.LENGTH_SHORT).show()
    }

}