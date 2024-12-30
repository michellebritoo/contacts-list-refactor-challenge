package com.list.desafio.android.di

import androidx.room.Room
import com.list.desafio.android.data.UsersRepository
import com.list.desafio.android.data.UsersRepositoryImpl
import com.list.desafio.android.data.local.AppDataBase
import com.list.desafio.android.data.local.dao.UserDao
import com.list.desafio.android.data.remote.RetrofitInstance
import com.list.desafio.android.data.remote.UsersClient
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val DataModule = module {
    factory { RetrofitInstance.getInstance().create(UsersClient::class.java) }
    factory<UsersRepository> { UsersRepositoryImpl(get(), get()) }

    single<AppDataBase> {
        Room.databaseBuilder(
            androidContext(),
            AppDataBase::class.java,
            "app_database"
        ).build()
    }
    single<UserDao> { get<AppDataBase>().userDao() }
}
