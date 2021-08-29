package com.example.candy.myPage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.candy.R
import com.example.candy.databinding.FragmentStudentCandyBinding
import com.example.candy.model.data.Candy
import com.example.candy.model.viewModel.SharedViewModel
import com.example.candy.utils.CurrentUser
import com.example.candy.utils.CustomDialog
import com.example.candy.utils.Util

class StudentCandyFragment : Fragment() {
    private lateinit var binding: FragmentStudentCandyBinding
    private val viewModel: MyPageViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var navController: NavController


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_student_candy, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)
        binding.titleBar.title.text = "캔디 인출"

        sharedViewModel.getCandyStudent().observe(viewLifecycleOwner, {
            val numberOfCandy = getString(R.string.numberOfStudentCandy, it)
            binding.candy = Candy(numberOfCandy)
        })
        binding.titleBar.backBtn.setOnClickListener {
            navController.popBackStack()
        }

        // 캔디 인출 버튼
        // TODO:: 캔디 획득 기능 구현 후 구현하기
        binding.withdrawCandy.setOnClickListener {
            if (sharedViewModel.getCandyStudent().value.equals("0")) {
                Util.toast(binding.root.context, "인출 가능한 캔디가 없습니다.")
                return@setOnClickListener
            }
            val dialog =
                CustomDialog(binding.root.context, sharedViewModel.getCandyStudent().value!!.toInt())
            dialog.myDialog(binding.root.context)

            dialog.setOnClickedListener(object : CustomDialog.ButtonClickListener {
                override fun onClicked(candy: Int) {

                }
            })
        }


    }
}