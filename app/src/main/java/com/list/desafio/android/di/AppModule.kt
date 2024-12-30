package com.list.desafio.android.di

import com.list.desafio.android.data.UsersRepository
import com.list.desafio.android.data.UsersRepositoryImpl
import com.list.desafio.android.data.remote.RetrofitInstance
import com.list.desafio.android.data.remote.UsersClient
import com.list.desafio.android.presentation.UsersViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val AppModule = module {
    viewModel { UsersViewModel(get()) }

    factory { RetrofitInstance.getInstance().create(UsersClient::class.java) }
    factory<UsersRepository> { UsersRepositoryImpl(client = get()) }
}
