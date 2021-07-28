package com.example.candy

import java.util.*
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage


class GMailSender(private val user: String, private val password: String) :
    javax.mail.Authenticator() {
    private val mailhost = "smtp.gmail.com"
    private val session: Session

    //생성된 이메일 인증코드 반환
    val emailCode: String

    private fun createEmailCode(): String { //이메일 인증코드 생성
        val str = arrayOf(
            "a", "b", "c", "d", "e", "f", "g", "h",
            "i", "j", "k", "l", "m", "n", "o", "p",
            "q", "r", "s", "t", "u", "v", "w", "x",
            "y", "z", "1", "2", "3", "4", "5", "6",
            "7","8", "9"
        )
        var newCode = String()
        for (x in 0..7) {
            val random = (Math.random() * str.size).toInt()
            newCode += str[random]
        }
        return newCode
    }

    //해당 메서드에서 사용자의 계정(id & password)을 받아 인증받으며 인증 실패시 기본값으로 반환됨.
    protected fun passwordAuthentication(): PasswordAuthentication{
        return PasswordAuthentication(user, password)
    }

    @Synchronized
    @Throws(Exception::class)
    fun sendMail(subject: String?, body: String, recipients: String) {
        val message = MimeMessage(session)

        message.sender = InternetAddress(user) //본인 이메일 설정
        message.subject = subject //해당 이메일의 본문 설정
        message.setText(body)
        if (recipients.indexOf(',') > 0) message.setRecipients(
            Message.RecipientType.TO,
            InternetAddress.parse(recipients)
        ) else message.setRecipient(Message.RecipientType.TO, InternetAddress(recipients))
        Transport.send(message) //메시지 전달
    }

    init {
        emailCode = createEmailCode()
        val props = Properties()

        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", mailhost);
        props.put("mail.smtp.auth","true");
        props.put("mail.smtp.port", "587");                 // mail 포트 465, 587
        props.put("mail.smtp.starttls","true");
        //구글에서 지원하는 smtp 정보를 받아와 MimeMessage 객체에 전달해준다.

        session = Session.getDefaultInstance(props,
            object : Authenticator() {
                override fun getPasswordAuthentication(): PasswordAuthentication? {
                    return passwordAuthentication()
                }
            })

    }
}

