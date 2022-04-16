package com.example.submission3.database

import android.provider.ContactsContract
import androidx.room.Dao
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ListUserDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(item: ListUserEntity)

    @Update
    fun update(item: ListUserEntity)

    @Delete
    fun delete(item: ListUserEntity)

    @Query("SELECT * from ListUserEntity ORDER BY id ASC")
    fun getAllListUsers(): LiveData<List<ListUserEntity>>

}