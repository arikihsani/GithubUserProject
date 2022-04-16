package com.example.submission3.ui

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.submission3.R
import com.example.submission3.database.ListUserEntity
import com.example.submission3.databinding.ActivityFavoriteUserBinding
import com.example.submission3.helper.ViewModelFactory

class FavoriteUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_Submission1)
        binding = ActivityFavoriteUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "List of Favorite User"

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)
        showLoading(true)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView


        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)

        val favoriteUserViewModel = obtainViewModel(this@FavoriteUserActivity)
        favoriteUserViewModel.getAllListUsers().observe(this, { listFavoriteUser ->
            if (listFavoriteUser != null && listFavoriteUser.isNotEmpty()) {
                setUserData(listFavoriteUser)
                showLoading(false)
            } else {
                binding.rvUsers.visibility = View.GONE
                binding.constraintLayout.visibility = View.VISIBLE
                showLoading(false)
            }
        })

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String): Boolean {

                searchView.clearFocus()
                val moveWithDataIntent =
                    Intent(this@FavoriteUserActivity, SearchActivity::class.java)
                moveWithDataIntent.putExtra(SearchActivity.EXTRA_SEARCH, query)
                startActivity(moveWithDataIntent)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu3 -> {
                val i = Intent(this, ThemeActivity::class.java)
                startActivity(i)
                true
            }
            else -> true
        }
    }

    private fun setUserData(listFavoriteUser: List<ListUserEntity>) {
        val arrayListFavoriteUser = java.util.ArrayList<User>()
        for (user in listFavoriteUser) {
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
            arrayListFavoriteUser.add(user_x)
        }
        binding.rvUsers.layoutManager = LinearLayoutManager(this)
        val adapter = RecyclerAdapter(arrayListFavoriteUser)
        binding.rvUsers.adapter = adapter

        adapter.setOnItemClickCallback(object : RecyclerAdapter.OnItemClickCallback {
            override fun onItemClicked(data: User) {
                showSelectedUser(data)
                val moveWithObjectIntent = Intent(this@FavoriteUserActivity, UserDetail::class.java)
                moveWithObjectIntent.putExtra(UserDetail.EXTRA_USER, data)
                startActivity(moveWithObjectIntent)

            }
        })
    }

    private fun obtainViewModel(activity: AppCompatActivity): FavoriteUserViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(FavoriteUserViewModel::class.java)
    }

    private fun showSelectedUser(user: User) {
        Toast.makeText(this, "Anda memilih " + user.username, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}