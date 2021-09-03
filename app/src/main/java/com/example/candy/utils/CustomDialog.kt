package com.example.candy.utils

import android.app.Dialog
import android.content.Context
import android.graphics.Point
import android.os.Build
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.WindowManager
import com.example.candy.databinding.InputLayoutBinding

class CustomDialog(context: Context,maxCandy: Int?) {
    private lateinit var binding: InputLayoutBinding
    private val dialog = Dialog(context)
    private val maxCandy = maxCandy.let {
        it
    }?: 0

    fun myDialog(context : Context,str: String,strInfo: String){
        binding = InputLayoutBinding.inflate(LayoutInflater.from(context))
        dialog.setContentView(binding.root)

        val info = binding.infoTv
        val okBtn = binding.okBtn
        val cancelBtn = binding.cancelBtn
        val candyNumber = binding.numberPicker
        candyNumber.minValue = 0
        candyNumber.maxValue = maxCandy

        info.text = strInfo
        okBtn.text = str

        // Set Size of Dialog
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        if(Build.VERSION.SDK_INT < 30){
            val display = windowManager.defaultDisplay
            val size = Point()
            display.getRealSize(size)

            val window = dialog.window
            val x = (size.x * 0.9f).toInt()

            window?.setLayout(x,WindowManager.LayoutParams.WRAP_CONTENT)
        }else{
            val rect = windowManager.currentWindowMetrics.bounds

            val window = dialog.window
            val x = (rect.width() * 0.9f).toInt()

            window?.setLayout(x,WindowManager.LayoutParams.WRAP_CONTENT)
        }

        dialog.setCancelable(true)

        dialog.show()

        okBtn.setOnClickListener {
            onClickedListener.onClicked(candyNumber.value)
            dialog.dismiss()
        }
        cancelBtn.setOnClickListener {
            dialog.dismiss()
        }
    }

    interface ButtonClickListener{
        fun onClicked(candy: Int)
    }

    private lateinit var onClickedListener: ButtonClickListener

    fun setOnClickedListener(listener: ButtonClickListener){
        onClickedListener = listener
    }
}