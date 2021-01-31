package com.hardtm.daggerpro.di

import com.hardtm.daggerpro.bash.FragmentBash
import com.hardtm.daggerpro.jokes.FragmentJokes
import dagger.Component
import javax.inject.Singleton

@Component(
    modules = [
        NetworkModule::class
    ],
    dependencies = [] //Components from other modules in app - data, presentation, utils
)

@Singleton
interface AppComponent {
    fun inject(fragment: FragmentBash)
    fun inject(fragment: FragmentJokes)
}