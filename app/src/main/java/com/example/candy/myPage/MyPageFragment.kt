package com.example.candy.myPage

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.candy.R
import com.example.candy.data.Candy
import com.example.candy.data.User
import com.example.candy.databinding.FragmentHomeBinding
import com.example.candy.databinding.FragmentMypageBinding
import com.example.candy.home.HomeFragment
import com.example.candy.utils.CurrentUser
import com.example.candy.viewModel.MainViewModel

class MyPageFragment: Fragment() {
    private lateinit var binding: FragmentMypageBinding
    private val viewModel: CandyViewModel by viewModels()

    companion object {
        const val TAG : String = "로그"

        fun newInstance() : MyPageFragment {
            return MyPageFragment()
        }

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d(TAG, "MyPageFragment - onAttach() called")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_mypage,container,false)
        Log.d(TAG, "MyPageFragment - onCreateView() called")
        return binding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.user = viewModel.getUserInfo()

        viewModel.getCandyStudent().observe(viewLifecycleOwner,{
            binding.candy = it
        })

        // 캔디 수 변경하는 임시 버튼
        binding.tempBtn.setOnClickListener {
            CurrentUser.userCandy.value = Candy("32")
        }

    }
}