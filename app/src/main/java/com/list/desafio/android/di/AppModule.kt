package com.list.desafio.android.di

import com.list.desafio.android.presentation.UsersViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val AppModule = module {
    viewModel { UsersViewModel(get()) }
}