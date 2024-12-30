package com.list.desafio.android.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.list.desafio.android.data.local.dao.UserDao
import com.list.desafio.android.data.local.entity.UserEntity

@Database(entities = [UserEntity::class], version = 1, exportSchema = false)
abstract class AppDataBase: RoomDatabase() {

    abstract fun userDao(): UserDao
}
