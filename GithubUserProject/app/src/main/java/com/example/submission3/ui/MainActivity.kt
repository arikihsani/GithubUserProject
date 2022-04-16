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
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.submission3.R
import com.example.submission3.databinding.ActivityMainBinding
import com.example.submission3.helper.ThemeViewModelFactory


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_Submission1)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = SettingPreferences.getInstance(dataStore)
        val themeViewModel = ViewModelProvider(this, ThemeViewModelFactory(pref)).get(
            ThemeViewModel::class.java
        )
        themeViewModel.getThemeSettings().observe(this,
            { isDarkModeActive: Boolean ->
                if (isDarkModeActive) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
            })

        supportActionBar?.show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView

        val mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            MainViewModel::class.java
        )
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)

        mainViewModel.user.observe(this, { user ->
            setUserData(user)
        })
        mainViewModel.isLoading.observe(this, {
            showLoading(it)
        })

        mainViewModel.loadInitialUser()

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String): Boolean {

                searchView.clearFocus()

                val moveWithDataIntent = Intent(this@MainActivity, SearchActivity::class.java)
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
            R.id.menu2 -> {
                val i = Intent(this, FavoriteUserActivity::class.java)
                startActivity(i)
                true
            }
            R.id.menu3 -> {
                val i = Intent(this, ThemeActivity::class.java)
                startActivity(i)
                true
            }
            else -> true
        }
    }


    private fun showSelectedUser(user: User) {
        Toast.makeText(this, "Anda memilih " + user.username, Toast.LENGTH_SHORT).show()
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
                val moveWithObjectIntent = Intent(this@MainActivity, UserDetail::class.java)
                moveWithObjectIntent.putExtra(UserDetail.EXTRA_USER, data)
                startActivity(moveWithObjectIntent)

            }
        })
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

}