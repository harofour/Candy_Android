package com.example.candy.utils

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.candy.GMailSender
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.onEach
import javax.mail.MessagingException
import javax.mail.SendFailedException

object Util {
    val Tag = "Util"
    fun sendMail(context: Context, email: String): String?{
        val gMailSender = GMailSender("candyauth@gmail.com", "candy123!")
        val code = gMailSender.emailCode
        try {
            toast(context, "이메일을 전송합니다. 잠시 기다려주세요.")
            //GMailSender.sendMail(제목, 본문내용, 받는사람);
            gMailSender.sendMail("Authentication Email from Candy", code, email)
            toast(context, "이메일을 성공적으로 보냈습니다.")
        } catch (e: SendFailedException) {
            toast(context, "이메일 형식이 잘못되었습니다.")
            return null
        } catch (e: MessagingException) {
            toast(context, "인터넷 연결을 확인해주십시오")
            e.printStackTrace()
            return null
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
        return code
    }

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