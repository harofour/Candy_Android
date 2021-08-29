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
import com.example.candy.databinding.FragmentParentCandyBinding
import com.example.candy.model.data.Candy
import com.example.candy.model.viewModel.SharedViewModel
import com.example.candy.utils.CurrentUser
import com.example.candy.utils.CustomDialog
import com.example.candy.utils.RESPONSE_STATE
import com.example.candy.utils.Util

class ParentCandyFragment : Fragment() {
    private lateinit var binding: FragmentParentCandyBinding
    private val viewModel: MyPageViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_parent_candy, container, false)

        sharedViewModel.getCandyParent().observe(viewLifecycleOwner, {
            val numberOfCandy = getString(R.string.numberOfParentCandy, it)
            binding.candy = Candy(numberOfCandy)
        })

        // 학부모 캔디 초기화
        sharedViewModel.getAPICandyParent(CurrentUser.userToken!!)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        navController = Navigation.findNavController(view)

        binding.titleBar.title.text = "캔디 충전"



        binding.titleBar.backBtn.setOnClickListener {
            navController.popBackStack()
        }

        // 캔디 충전 버튼
        binding.chargeCandy.setOnClickListener {
            val dialog = CustomDialog(binding.root.context, 100)
            dialog.myDialog(binding.root.context)

            dialog.setOnClickedListener(object : CustomDialog.ButtonClickListener {
                override fun onClicked(candy: Int) {
                    val reqData = HashMap<String, Int>()
                    reqData["amount"] = candy
                    sharedViewModel.updateCandyParent(CurrentUser.userToken!!, reqData)
                }
            })
        }

    }
}