package com.kova700.zerotomvvm.util

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import java.io.Serializable

fun <T : Serializable> Intent.getSerializableExtraData(key: String, clazz: Class<T>): T? =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getSerializableExtra(key, clazz)
    } else {
        getSerializableExtra(key) as T?
    }

fun Intent.getBooleanExtraData(key: String): Boolean = getBooleanExtra(key, false)
fun Intent.getIntExtraData(key: String): Int = getIntExtra(key, -1)

fun <T : Serializable> Activity.finishWithSerializableExtra(key: String, extraData: T) {
    intent.putExtra(key, extraData)
    setResult(AppCompatActivity.RESULT_OK, intent)
    finish()
}

fun Activity.finishWithExtra() {
    setResult(AppCompatActivity.RESULT_OK, intent)
    finish()
}

fun Activity.showSnackBar(layout: View, anchorView: View, textId: Int) {
    Snackbar.make(layout, textId, Snackbar.LENGTH_SHORT)
        .setAnchorView(anchorView)
        .show()
}