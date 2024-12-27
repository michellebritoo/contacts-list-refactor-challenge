package com.list.desafio.android.di

import com.list.desafio.android.UsersClient
import com.list.desafio.android.data.RetrofitInstance
import org.koin.dsl.module

val DataModule = module {
    factory { RetrofitInstance.getInstance().create(UsersClient::class.java) }
}
