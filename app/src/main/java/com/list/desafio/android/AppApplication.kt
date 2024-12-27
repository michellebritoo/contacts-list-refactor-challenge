package com.list.desafio.android

import android.app.Application
import com.list.desafio.android.di.DataModule
import com.list.desafio.android.di.PresentationModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class AppApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@AppApplication)
            modules(
                DataModule,
                PresentationModule
            )
        }
    }
}
