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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.candy.R
import com.example.candy.databinding.FragmentStudentCandyBinding
import com.example.candy.model.data.Candy
import com.example.candy.model.viewModel.SharedViewModel
import com.example.candy.myPage.adapter.HistoryAdapter
import com.example.candy.myPage.viewmodel.MyPageViewModel
import com.example.candy.utils.CurrentUser
import com.example.candy.utils.CustomDialog
import com.example.candy.utils.Util

class StudentCandyFragment : Fragment() {
    private lateinit var binding: FragmentStudentCandyBinding
    private val viewModel: MyPageViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var navController: NavController
    private lateinit var historyAdapter: HistoryAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_student_candy, container, false)

        initStudentCandy()

        initStudentCandyHistory()

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        initTitleBar()

        // 캔디 인출 버튼
        initWithdrawCandyButton()

        setRecyclerview()


    }



    private fun initTitleBar() {
        binding.titleBar.title.text = "캔디 인출"
        binding.titleBar.backBtn.setOnClickListener {
            navController.popBackStack()
        }
    }

    private fun initWithdrawCandyButton() {
        binding.withdrawCandy.setOnClickListener {
            if (sharedViewModel.getCandyStudent().value.equals("0")) {
                Util.toast(binding.root.context, "인출 가능한 캔디가 없습니다.")
                return@setOnClickListener
            }
            val dialog =
                CustomDialog(binding.root.context, sharedViewModel.getCandyStudent().value!!.toInt())
            dialog.myDialog(binding.root.context,"인출","인출할 캔디 개수를 선택해 주세요.")

            dialog.setOnClickedListener(object : CustomDialog.ButtonClickListener {
                override fun onClicked(candy: Int) {
                    val reqData = HashMap<String, Int>()
                    reqData["amount"] = candy
                    sharedViewModel.updateCandyStudent(CurrentUser.userToken!!,reqData)
                }
            })
        }
    }

    private fun setRecyclerview() {
        binding.recyclerview.adapter = historyAdapter
        binding.recyclerview.layoutManager =
            LinearLayoutManager(binding.root.context, LinearLayoutManager.VERTICAL, false)
        binding.recyclerview.setHasFixedSize(false)
    }


    private fun initStudentCandyHistory() {
        // 캔디 내역 세팅
        historyAdapter = HistoryAdapter()
        viewModel.getStudentHistories().observe(viewLifecycleOwner, {
            historyAdapter.submitList(it)
            binding.recyclerview.smoothScrollToPosition(0)
        })
    }

    private fun initStudentCandy() {
        sharedViewModel.getCandyStudent().observe(viewLifecycleOwner, {
            val numberOfCandy = getString(R.string.numberOfStudentCandy, it)
            binding.candy = Candy(numberOfCandy)
            viewModel.getAPIHistoryData(CurrentUser.userToken!!, "student", "all", "100000", "20")
        })
    }
}