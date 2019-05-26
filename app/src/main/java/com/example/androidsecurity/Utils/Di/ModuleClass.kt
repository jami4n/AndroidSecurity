package com.example.androidsecurity.Utils.Di

import android.app.Application
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.example.androidsecurity.Utils.SharedPrefHelper
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ModuleClass(val app:Application){

    @Provides
    @Singleton
    fun getSharedPreferences():SharedPreferences{
        return app.getSharedPreferences("Shar",MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun getSharedPrefHelper():SharedPrefHelper{
        return SharedPrefHelper(getSharedPreferences())
    }

}