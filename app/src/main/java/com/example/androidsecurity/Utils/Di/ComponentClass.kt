package com.example.androidsecurity.Utils.Di

import com.example.androidsecurity.UI.Home.HomeActivity
import com.example.androidsecurity.UI.Main.MainActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ModuleClass::class])
interface ComponentClass{

    fun inject(mainActivity: MainActivity)
    fun inject(homeActivity: HomeActivity)
}