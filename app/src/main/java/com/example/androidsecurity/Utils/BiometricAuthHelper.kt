package com.example.androidsecurity.Utils

import android.content.Context
import androidx.fragment.app.FragmentActivity
import com.example.androidsecurity.UI.Main.BiometricCallback

object BiometricAuthHelper {

        fun authenticateViaFingerPrint(context: Context, biometricCallback: BiometricCallback){
            FingerPrintHelper.loadFingerprintIfAvaiable(context,biometricCallback)
        }

        fun authenticateViaBiometricPromt(activity: FragmentActivity, biometricCallback: BiometricCallback){
            BiometricHelper.loadBiometric(activity,biometricCallback)
        }


}