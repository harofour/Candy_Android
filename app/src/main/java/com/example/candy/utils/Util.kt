package com.example.candy.utils

import android.content.Context
import android.widget.Toast
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
}