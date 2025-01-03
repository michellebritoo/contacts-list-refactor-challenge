package com.list.desafio.android

import android.app.Application
import com.list.desafio.android.di.AppModule
import com.list.desafio.android.di.DataModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class AppApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@AppApplication)
            modules(
                AppModule,
                DataModule
            )
        }
    }
}
