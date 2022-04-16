package com.example.submission3.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.submission3.R
import com.example.submission3.database.ListUserEntity
import com.example.submission3.databinding.ActivityUserDetailBinding
import com.example.submission3.helper.ViewModelFactory
import com.google.android.material.tabs.TabLayoutMediator

class UserDetail : AppCompatActivity() {

    private lateinit var binding: ActivityUserDetailBinding
    private lateinit var favoriteUserViewModel: FavoriteUserViewModel
    private var listUserEntity: List<ListUserEntity> = listOf()
    private lateinit var adapter: RecyclerAdapter

    companion object {
        const val EXTRA_USER = "extra_user"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_Submission1)
        supportActionBar?.hide()
        binding = ActivityUserDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        favoriteUserViewModel = obtainViewModel(this@UserDetail)

        val userDetailViewModel =
            ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
                UserDetailViewModel::class.java
            )

        userDetailViewModel.userDetails.observe(this, { userDetails ->
            setUserDetails(userDetails)
        })
        userDetailViewModel.isLoading.observe(this, {
            showLoading(it)
        })

        val user = intent.getParcelableExtra<User>(EXTRA_USER) as User
        userDetailViewModel.findUserDetails(user.username)
        favoriteUserViewModel.getAllListUsers().observe(this, {
            for (item in it) {
                if (item.username.equals(user.username)) {
                    user.isFavorite = true
                }
            }
            listUserEntity = it
            if (user.isFavorite == true) {
                binding.button.text = "Remove from Favorite"
            } else {
                binding.button.text = "Add to Favorite"
            }
        })

        binding.button.setOnClickListener {
            if (user.isFavorite == true) {
                for (userDb in listUserEntity) {
                    if (user.username.equals(userDb.username)) {
                        favoriteUserViewModel.delete(userDb)
                    }
                }
                user.isFavorite = false
                binding.button.text = "Add to Favorite" // setelah delete balikin lagi
                Toast.makeText(
                    this,
                    "Removed " + user.username + " from Favorite",
                    Toast.LENGTH_SHORT
                ).show()

            } else {
                var dbUser = ListUserEntity(
                    username = user.username,
                    avatar_url = user.avatar_url,
                    html_url = user.html_url
                )
                favoriteUserViewModel.insert(dbUser)
                user.isFavorite = true
                binding.button.text = "Remove from Favorite" // setelah diadd button nya ganti
                Toast.makeText(this, "Added " + user.username + " to Favorite", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun setUserDetails(fromApi: User) {
        binding.layoutUserDetail.nameValue.text = fromApi.name ?: "null"
        binding.layoutUserDetail.usernameValue.text = fromApi.username ?: "null"
        binding.layoutUserDetail.locationValue.text = fromApi.location ?: "null"
        binding.layoutUserDetail.repositoryValue.text = fromApi.repository ?: "null"
        binding.layoutUserDetail.companyValue.text = fromApi.company ?: "null"
        binding.layoutUserDetail.followersValue.text = fromApi.followers ?: "null"
        binding.layoutUserDetail.followingValue.text = fromApi.following ?: "null"
        Glide.with(binding.userImage.context)
            .load(fromApi.avatar_url)
            .circleCrop()
            .into(binding.userImage)

        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        binding.viewPager.adapter = sectionsPagerAdapter
        sectionsPagerAdapter.username = fromApi.username
        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
        supportActionBar?.elevation = 0f

    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun obtainViewModel(activity: AppCompatActivity): FavoriteUserViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(FavoriteUserViewModel::class.java)
    }

}