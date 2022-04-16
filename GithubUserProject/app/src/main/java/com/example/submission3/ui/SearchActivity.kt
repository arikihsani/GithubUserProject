package com.example.submission3.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.submission3.R
import com.example.submission3.databinding.ActivitySearchBinding


class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding

    companion object {
        const val EXTRA_SEARCH = "search_query"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_Submission1)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Search Result"

        supportActionBar?.show()
        showLoading(true)
        showLayout()
    }

    private fun showLayout() {
        val userID = intent.getStringExtra(EXTRA_SEARCH).toString()
        val mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            MainViewModel::class.java
        )
        mainViewModel.findUser(userID)

        mainViewModel.user.observe(this, { user ->
            if (user != null && user.isNotEmpty()) {
                setUserData(user)
            } else {
                binding.rvUsers.visibility = View.GONE
                binding.constraintLayout.visibility = View.VISIBLE
                showLoading(false)
            }
        })
        mainViewModel.isLoading.observe(this, {
            showLoading(it)
        })

        mainViewModel.findUser(userID)
    }

    private fun setUserData(listFromApiUser: List<User>) {

        val listSearchedUser = java.util.ArrayList<User>()
        for (user in listFromApiUser) {
            val user_x = User(
                null,
                user.username,
                null,
                null,
                null,
                null,
                null,
                user.avatar_url,
                user.html_url,
                null
            )
            listSearchedUser.add(user_x)
        }
        binding.rvUsers.layoutManager = LinearLayoutManager(this)
        val adapter = RecyclerAdapter(listSearchedUser)
        binding.rvUsers.adapter = adapter

        adapter.setOnItemClickCallback(object : RecyclerAdapter.OnItemClickCallback {
            override fun onItemClicked(data: User) {
                showSelectedUser(data)
                val moveWithObjectIntent = Intent(this@SearchActivity, UserDetail::class.java)
                moveWithObjectIntent.putExtra(UserDetail.EXTRA_USER, data)
                startActivity(moveWithObjectIntent)

            }
        })
    }

    private fun showSelectedUser(user: User) {
        Toast.makeText(this, "Anda memilih " + user.username, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

}