package com.example.submission3.ui

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.ViewModel
import com.example.submission3.database.ListUserEntity
import com.example.submission3.repository.ListUserRepository

class FavoriteUserViewModel(application: Application) : ViewModel() {
    private val mListUserRepository: ListUserRepository = ListUserRepository(application)
    fun getAllListUsers(): LiveData<List<ListUserEntity>> = mListUserRepository.getAllListUsers()

    fun insert(user: ListUserEntity) {
        mListUserRepository.insert(user)
    }

    fun delete(user: ListUserEntity) {
        mListUserRepository.delete(user)
    }

}