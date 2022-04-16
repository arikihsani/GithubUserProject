package com.example.submission3.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ListUserEntity::class], version = 1)
abstract class ListUserRoomDatabase : RoomDatabase() {

    abstract fun noteDao(): ListUserDAO

    companion object {
        @Volatile
        private var INSTANCE: ListUserRoomDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): ListUserRoomDatabase {
            if (INSTANCE == null) {
                synchronized(ListUserRoomDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        ListUserRoomDatabase::class.java, "ListUserEntity_database")
                        .build()
                }
            }
            return INSTANCE as ListUserRoomDatabase
        }
    }

}