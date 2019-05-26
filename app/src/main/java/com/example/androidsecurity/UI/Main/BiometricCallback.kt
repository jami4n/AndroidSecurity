package com.example.androidsecurity.UI.Main

interface BiometricCallback{
    fun onBiometricAuthSuccess()
    fun onBiometricAuthFailed(e:String = "")
}