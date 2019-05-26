package com.example.androidsecurity.Utils

import android.app.Activity
import android.view.View
import com.google.android.material.snackbar.Snackbar

fun String.makeSnackBar(view: View){
    Snackbar.make(view,this,Snackbar.LENGTH_SHORT).show()
}
