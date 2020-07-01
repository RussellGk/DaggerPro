package com.hardtm.daggerpro.di

import android.app.Application
import com.hardtm.daggerpro.MainActivity
import com.hardtm.daggerpro.mvp.FragmentOne
import com.hardtm.daggerpro.mvvm.FragmentTwo
import dagger.Binds
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Component(
    modules = [
        NetworkModule::class,
        DatabaseModule::class,
        AppModule::class
    ],
    dependencies = []
)
@Singleton
interface AppComponent {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

    fun inject(activity: MainActivity)

    fun inject(fragment: FragmentTwo)
    fun inject(fragment: FragmentOne)
}