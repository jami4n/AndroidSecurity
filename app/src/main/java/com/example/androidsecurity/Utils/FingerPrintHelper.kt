package com.example.androidsecurity.Utils

import android.annotation.TargetApi
import android.app.KeyguardManager
import android.content.Context
import android.content.Context.FINGERPRINT_SERVICE
import android.content.Context.KEYGUARD_SERVICE
import android.content.pm.PackageManager
import android.hardware.fingerprint.FingerprintManager
import android.os.Build
import android.os.CancellationSignal
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import com.example.androidsecurity.UI.Main.BiometricCallback
import java.lang.Exception
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey

@TargetApi(Build.VERSION_CODES.M)
object FingerPrintHelper : FingerprintManager.AuthenticationCallback() {

    val FINGER_KEY = "finger_key"
    val fingerPrintBaseError = "Your device does not support fingerprint service"
    lateinit var cipher: Cipher
    lateinit var keyStore:KeyStore
    lateinit var keyGenerator: KeyGenerator
    lateinit var cryptoObject:FingerprintManager.CryptoObject
    lateinit var fingerprintManager: FingerprintManager
    lateinit var keyguardManager: KeyguardManager
    lateinit var cancellationSignal:CancellationSignal

    lateinit var biometricCallback:BiometricCallback

    fun loadFingerprintIfAvaiable(context: Context, biometricCallback: BiometricCallback):String{

        this.biometricCallback = biometricCallback

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){

            keyguardManager = context.getSystemService(KEYGUARD_SERVICE) as KeyguardManager
            fingerprintManager = context.getSystemService(FINGERPRINT_SERVICE) as FingerprintManager

            if(!fingerprintManager.isHardwareDetected)
                biometricCallback.onBiometricAuthFailed(fingerPrintBaseError)

            if(ActivityCompat.checkSelfPermission(context,android.Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED)
                biometricCallback.onBiometricAuthFailed("Please enable fingerprint permission")

            if(!fingerprintManager.hasEnrolledFingerprints())
                biometricCallback.onBiometricAuthFailed("No fingerprints found, please register atleast one fingerprint in device settings.")

            if(!keyguardManager.isKeyguardSecure)
                biometricCallback.onBiometricAuthFailed("Please enable lockscreen security on your device's settings")


            try { generateKey() }catch (e:Exception){ }


        }else{
            biometricCallback.onBiometricAuthFailed(fingerPrintBaseError)
        }

        return ""
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun generateKey(){

        keyStore = KeyStore.getInstance("AndroidKeyStore")
        keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES,"AndroidKeyStore")

        keyStore.load(null)
        keyGenerator.init(
            KeyGenParameterSpec
                .Builder(FINGER_KEY,  KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
                .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                .setUserAuthenticationRequired(true)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                .build()
        )

        keyGenerator.generateKey()

        if(initCipher()){
            cryptoObject = FingerprintManager.CryptoObject(cipher)
            startAuth(fingerprintManager, cryptoObject)
        }

    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun initCipher():Boolean{

        try {

            cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/" + KeyProperties.BLOCK_MODE_CBC  + "/" + KeyProperties.ENCRYPTION_PADDING_PKCS7)

            keyStore.load(null)
            var key = keyStore.getKey(FINGER_KEY,null) as SecretKey
            cipher.init(Cipher.ENCRYPT_MODE,key)
            return true

        }catch (e: Exception) {

            Log.d("12345",e.toString())
        }

        return false

    }

    private fun startAuth(manager:FingerprintManager,cryptoObject: FingerprintManager.CryptoObject){
        cancellationSignal = CancellationSignal()
        manager.authenticate(cryptoObject, cancellationSignal,0,this,null)

    }

    override fun onAuthenticationError(errorCode: Int, errString: CharSequence?) {
        super.onAuthenticationError(errorCode, errString)
        cancellationSignal.cancel()
        biometricCallback.onBiometricAuthFailed(errString.toString())
    }

    override fun onAuthenticationSucceeded(result: FingerprintManager.AuthenticationResult?) {
        super.onAuthenticationSucceeded(result)
        Log.d("12345","Auth Successful")
        cancellationSignal.cancel()
        biometricCallback.onBiometricAuthSuccess()
    }

    override fun onAuthenticationHelp(helpCode: Int, helpString: CharSequence?) {
        super.onAuthenticationHelp(helpCode, helpString)
        biometricCallback.onBiometricAuthFailed(helpString.toString())
    }

    override fun onAuthenticationFailed() {
        super.onAuthenticationFailed()
        cancellationSignal.cancel()
        biometricCallback.onBiometricAuthFailed("Authentication failed")
    }
}