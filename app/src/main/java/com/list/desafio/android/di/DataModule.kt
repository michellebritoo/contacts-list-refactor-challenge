package com.list.desafio.android.di

import com.list.desafio.android.data.remote.UsersClient
import com.list.desafio.android.data.remote.RetrofitInstance
import com.list.desafio.android.data.UsersRepository
import com.list.desafio.android.data.UsersRepositoryImpl
import org.koin.dsl.module

val DataModule = module {
    factory { RetrofitInstance.getInstance().create(UsersClient::class.java) }
    factory<UsersRepository> { UsersRepositoryImpl(get()) }
}
