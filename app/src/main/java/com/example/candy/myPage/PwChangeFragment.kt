package com.example.candy.myPage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.candy.R
import com.example.candy.databinding.FragmentPwChangeBinding

class PwChangeFragment : Fragment() {
    private lateinit var binding: FragmentPwChangeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_pw_change,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.titleBar.title.text = "비밀번호 변경"
        binding.titleBar.backBtn.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

    }
}