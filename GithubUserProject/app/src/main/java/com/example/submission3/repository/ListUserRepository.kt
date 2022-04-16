package com.example.submission3.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.submission3.database.ListUserDAO
import com.example.submission3.database.ListUserEntity
import com.example.submission3.database.ListUserRoomDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class ListUserRepository(application: Application) {
    private val mListUsersDao: ListUserDAO
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()
    init {
        val db = ListUserRoomDatabase.getDatabase(application)
        mListUsersDao = db.noteDao()
    }
    fun getAllListUsers(): LiveData<List<ListUserEntity>> = mListUsersDao.getAllListUsers()
    fun insert(note: ListUserEntity) {
        executorService.execute { mListUsersDao.insert(note) }
    }
    fun delete(note: ListUserEntity) {
        executorService.execute { mListUsersDao.delete(note) }
    }
    fun update(note: ListUserEntity) {
        executorService.execute { mListUsersDao.update(note) }
    }
}