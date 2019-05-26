package com.example.androidsecurity.UI.Home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.androidsecurity.R
import com.example.androidsecurity.Utils.ApplicationClass
import com.example.androidsecurity.Utils.SharedPrefHelper
import kotlinx.android.synthetic.main.activity_home.*
import javax.inject.Inject

class HomeActivity :AppCompatActivity(){

    @Inject
    lateinit var sharedPref:SharedPrefHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as ApplicationClass).componentClass.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        tvHome.text = "Thanks for logging in, " + sharedPref.getUserLoggedInName()
        tvLogout.setOnClickListener {
            sharedPref.setPrivateAccessToken("")
            sharedPref.setUserLoggedIn("")
            finish()

        }



    }
}