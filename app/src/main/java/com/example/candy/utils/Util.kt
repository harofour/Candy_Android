package com.example.candy.utils

import android.content.Context
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import kotlinx.coroutines.*

object Util {
    val Tag = "Util"

    fun toast(context: Context, text: String){
        CoroutineScope(Dispatchers.Main).launch{
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
        }
    }

    fun timer(times: Int,timeMillis: Long, action: (Int) -> (Unit), finished: () -> (Unit)){
        CoroutineScope(Dispatchers.Default).launch{
            repeat(times){
                delay(timeMillis)
                action(it)
            }
            finished()
        }
    }

    fun showErrorAlertDialog(context: Context,title: String,message: String) {
        AlertDialog.Builder(context)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("확인") { _, _ ->
            }
            .create()
            .show()
    }
}