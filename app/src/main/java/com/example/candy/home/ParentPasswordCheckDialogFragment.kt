package com.example.candy.home

import android.content.Context
import android.graphics.Point
import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.example.candy.databinding.FragmentDialogParentPasswordCheckBinding
import com.example.candy.utils.DIALOG_REQUEST_KEY
import com.example.candy.utils.Util

class ParentPasswordCheckDialogFragment : DialogFragment() {
    private lateinit var binding: FragmentDialogParentPasswordCheckBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentDialogParentPasswordCheckBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 스크린 크기
        val wm: WindowManager = context?.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val screen: Display = wm.defaultDisplay

        val size = Point()
        screen.getRealSize(size)
        val height = size.y
        val width = size.x

        //다이얼로그 크기
        binding.dialogLayout.layoutParams.width = width * 310 / 375
        binding.dialogLayout.layoutParams.height = width * 415 / 375


        binding.okBtn.setOnClickListener {
            val pwd = binding.pwEditText.text.toString()
            if(pwd.isEmpty()){
                activity?.let { Util.toast(it.applicationContext, "학부모 비밀번호를 입력해 주세요") }
            }else{
                val bundle = Bundle()
                bundle.putString("ParentPassword",binding.pwEditText.text.toString())
                setFragmentResult(DIALOG_REQUEST_KEY, bundle)
                dismiss()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        val bundle = Bundle()
        bundle.putBoolean("Verified",false)
        setFragmentResult(DIALOG_REQUEST_KEY, bundle)
    }
}