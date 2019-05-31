package com.example.androidsecurity.UI.Main

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.FragmentActivity
import com.bumptech.glide.Glide
import com.example.androidsecurity.R
import com.example.androidsecurity.UI.Home.HomeActivity
import com.example.androidsecurity.Utils.ApplicationClass
import com.example.androidsecurity.Utils.BiometricAuthHelper.authenticateViaBiometricPromt
import com.example.androidsecurity.Utils.BiometricAuthHelper.authenticateViaFingerPrint
import com.example.androidsecurity.Utils.EncryptionHelper
import com.example.androidsecurity.Utils.SharedPrefHelper
import com.example.androidsecurity.Utils.makeSnackBar
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : AppCompatActivity(),BiometricCallback {

    @Inject
    lateinit var sharedPref: SharedPrefHelper

    @Inject
    lateinit var encryption: EncryptionHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as ApplicationClass).componentClass.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Glide.with(this).asGif().load(R.drawable.robot).into(ivLogo)

        btnLogin.setOnClickListener {
            validateLoginWithCredentials(etUsername.text.toString(),etPassword.text.toString())
        }
    }

    private fun checkUserLoggedIn(){
        if(sharedPref.getPrivateAccessToken().isNotEmpty()){
            tvLoginPrompt.text = "Hello ${sharedPref.getUserLoggedInName()}, Please login with credentials or biometrics."
            activateBiometricAuthentication()
        }else{
            tvLoginPrompt.text = "Hello, Please login to continue."
        }
    }

    override fun onResume() {
        super.onResume()
        checkUserLoggedIn()
    }


    private fun validateLoginWithCredentials(username: String, password: String) {

        var users = getDummyUsers()

        if(users.containsKey(username)){

            if(users.get(username).equals(password)){

                sharedPref.setPrivateAccessToken("Aq3heib23ir23203f8474h2349234f98")
                sharedPref.setUserLoggedIn(encryption.encrypt(etUsername.text.toString()))

                etUsername.text?.clear()
                etPassword.text?.clear()


                startActivity(Intent(this,HomeActivity::class.java))

            }else{
                "The username and password do not match".makeSnackBar(btnLogin)
            }

        }else{
            "User does not exist".makeSnackBar(btnLogin)
        }
    }

    private fun getDummyUsers():HashMap<String,String>{
        var users = HashMap<String,String>()
        users.put("jamian","12345")
        users.put("robot","robot")

        return users

    }

    private fun activateBiometricAuthentication(){

        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.M){
            val activity: FragmentActivity = this
            authenticateViaBiometricPromt(activity,this)
        }else{
            authenticateViaFingerPrint(this,this)
        }

    }

    override fun onBiometricAuthSuccess() {
        "Fingerprint Authentication succesful".makeSnackBar(btnLogin)

        Handler().postDelayed({
            sharedPref.setPrivateAccessToken("Aq3heib23ir23203f8474h2349234f98")
            startActivity(Intent(this,HomeActivity::class.java))
        },1000)


    }

    override fun onBiometricAuthFailed(e: String) {
        e.makeSnackBar(btnLogin)
        Log.d("12345","Auth Error " + e)
    }
}