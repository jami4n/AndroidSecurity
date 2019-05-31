package com.example.androidsecurity.Utils

import android.app.Activity
import android.content.Context
import androidx.biometric.BiometricPrompt
import androidx.fragment.app.FragmentActivity
import com.example.androidsecurity.UI.Main.BiometricCallback
import java.util.concurrent.Executors

object BiometricHelper{

    fun loadBiometric(activity: FragmentActivity, biometricCallback: BiometricCallback){

        val executor = Executors.newSingleThreadExecutor()
        val biometricPrompt = BiometricPrompt(activity,executor, object : BiometricPrompt.AuthenticationCallback(){
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)

                if (errorCode == BiometricPrompt.ERROR_NEGATIVE_BUTTON){
                    biometricCallback.onBiometricAuthFailed("Alternatively, you may log in with your credentials")
                }else{
                    biometricCallback.onBiometricAuthFailed(errString.toString())
                }
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                biometricCallback.onBiometricAuthFailed("Authentication failed")
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)

                biometricCallback.onBiometricAuthSuccess()
            }

        })

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
                        .setTitle("Biometric Authentication")
                        .setSubtitle("Subtitle")
                        .setDescription("You need to authenticate to move ahead")
                        .setNegativeButtonText("Fuck off")
                        .build()

        biometricPrompt.authenticate(promptInfo)

    }

}