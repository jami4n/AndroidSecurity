package com.example.androidsecurity.Utils

import android.content.SharedPreferences
import android.util.Log
import javax.inject.Inject

class SharedPrefHelper(val preferences: SharedPreferences){

    val ACCESS_TOKEN_NAME = "token"
    val USER_NAME = "user"


    fun setPrivateAccessToken(token:String){
        preferences.edit().putString(ACCESS_TOKEN_NAME, token).commit()
    }

    fun setUserLoggedIn(user:String){
        preferences.edit().putString(USER_NAME, user).commit()
    }

    fun getPrivateAccessToken():String{
        return preferences.getString(ACCESS_TOKEN_NAME,"")
    }

    fun getUserLoggedInName():String{
        return preferences.getString(USER_NAME,"")
    }

}