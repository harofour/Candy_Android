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
import com.example.candy.databinding.FragmentParentCandyBinding
import com.example.candy.model.data.Candy
import com.example.candy.model.viewModel.SharedViewModel
import com.example.candy.myPage.adapter.HistoryAdapter
import com.example.candy.myPage.viewmodel.MyPageViewModel
import com.example.candy.utils.CurrentUser
import com.example.candy.utils.CustomDialog

class ParentCandyFragment : Fragment() {
    private lateinit var binding: FragmentParentCandyBinding
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
            DataBindingUtil.inflate(inflater, R.layout.fragment_parent_candy, container, false)

        initParentCandy()

        initParentCandyHistory()

        return binding.root
    }




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        navController = Navigation.findNavController(view)

        initTitleBar()

        initChargeCandyButton()

        setRecyclerview()

    }



    private fun setRecyclerview() {
        binding.recyclerview.adapter = historyAdapter
        binding.recyclerview.layoutManager =
            LinearLayoutManager(binding.root.context, LinearLayoutManager.VERTICAL, false)
        binding.recyclerview.setHasFixedSize(false)
    }

    private fun initChargeCandyButton() {
        // 캔디 충전 버튼
        binding.chargeCandy.setOnClickListener {
            val dialog = CustomDialog(binding.root.context, 100)
            dialog.myDialog(binding.root.context,"충전","충전할 캔디 개수를 선택해 주세요.")

            dialog.setOnClickedListener(object : CustomDialog.ButtonClickListener {
                override fun onClicked(candy: Int) {
                    val reqData = HashMap<String, Int>()
                    reqData["amount"] = candy
                    sharedViewModel.updateCandyParent(CurrentUser.userToken!!, reqData)
                }

            })
        }
    }

    private fun initTitleBar() {
        // 타이틀바 세팅
        binding.titleBar.title.text = "캔디 충전"
        binding.titleBar.backBtn.setOnClickListener {
            navController.popBackStack()
        }
    }

    private fun initParentCandy() {
        sharedViewModel.getCandyParent().observe(viewLifecycleOwner, {
            val numberOfCandy = getString(R.string.numberOfParentCandy, it)
            binding.candy = Candy(numberOfCandy)
            viewModel.getAPIHistoryData(CurrentUser.userToken!!, "parent", "all", "100000", "20")
        })
        // 학부모 캔디 초기화
        sharedViewModel.getAPICandyParent(CurrentUser.userToken!!)
    }

    private fun initParentCandyHistory() {
        // 캔디 내역 세팅
        historyAdapter = HistoryAdapter()
        viewModel.getParentHistories().observe(viewLifecycleOwner, {
            historyAdapter.submitList(it)
            binding.recyclerview.smoothScrollToPosition(0)
        })
    }
}