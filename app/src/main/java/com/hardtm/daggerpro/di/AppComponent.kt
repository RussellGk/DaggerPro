package com.hardtm.daggerpro.di

import com.hardtm.daggerpro.MainActivity
import com.hardtm.daggerpro.features.bash.BashModule
import com.hardtm.daggerpro.mvvm.FragmentTwo
import dagger.Component
import javax.inject.Singleton

@Component(
    modules = [
        NetworkModule::class,
        BashModule::class
    ],
    dependencies = []
)
@Singleton
interface AppComponent {

    fun inject(activity: MainActivity)

    fun inject(fragment: FragmentTwo)
}