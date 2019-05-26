package com.example.androidsecurity.Utils

import android.app.Application
import com.example.androidsecurity.Utils.Di.ComponentClass
import com.example.androidsecurity.Utils.Di.DaggerComponentClass
import com.example.androidsecurity.Utils.Di.ModuleClass

class ApplicationClass: Application(){

    lateinit var componentClass: ComponentClass

    override fun onCreate() {
        super.onCreate()
        componentClass = initDagger(this)
    }

    private fun initDagger(app:ApplicationClass): ComponentClass =
        DaggerComponentClass.builder()
            .moduleClass(ModuleClass(app))
            .build()


}