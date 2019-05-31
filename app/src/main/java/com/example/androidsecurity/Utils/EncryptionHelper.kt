package com.example.androidsecurity.Utils

import android.util.Base64
import android.util.Log
import java.security.MessageDigest
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

class EncryptionHelper{

    var appSecretKey = "Aw4jb5w54yDI235433w54ybfse9433BJHKS" //this is vulnerable as hell!


    fun encrypt(text:String):String{
        val key = generateKey()
        val c = Cipher.getInstance("AES");
        c.init(Cipher.ENCRYPT_MODE,key)

        val encValue = c.doFinal(text.toByteArray())
        val encryptedValue = Base64.encodeToString(encValue,Base64.DEFAULT)
        decrypt(encryptedValue)

        return encryptedValue
    }

    fun decrypt(encryptedText:String):String{
        val key = generateKey()
        val cipher = Cipher.getInstance("AES")
        cipher.init(Cipher.DECRYPT_MODE,key)

        val decodedValue = Base64.decode(encryptedText.trim(),Base64.DEFAULT)
        val decValue = cipher.doFinal(decodedValue)
        val decryptedValue = String(decValue)
        return decryptedValue
    }

    private fun generateKey(): SecretKeySpec {
        val messageDigest = MessageDigest.getInstance("SHA-256")
        val bytes = appSecretKey.toByteArray()
        messageDigest.update(bytes,0, bytes.size)
        val key = messageDigest.digest()
        val spec = SecretKeySpec(key,"AES")
        return spec
    }

}