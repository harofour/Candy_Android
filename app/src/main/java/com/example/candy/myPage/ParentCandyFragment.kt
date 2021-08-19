package com.example.candy.myPage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.candy.R
import com.example.candy.databinding.FragmentParentCandyBinding
import com.example.candy.utils.CurrentUser
import com.example.candy.utils.CustomDialog
import com.example.candy.utils.Util

class ParentCandyFragment : Fragment() {
    private lateinit var binding: FragmentParentCandyBinding
    private val viewModel: CandyViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_parent_candy,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        binding.titleBar.title.text = "캔디 충전"
        binding.studentCandy.text = CurrentUser.parentCandy.value?.candy

        viewModel.getCandyParent().observe(viewLifecycleOwner,{
            binding.candy = it
        })

        binding.titleBar.backBtn.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        // 캔디 충전 버튼
        binding.chargeCandy.setOnClickListener {
            val dialog = CustomDialog(binding.root.context,100)
            dialog.myDialog(binding.root.context)

            dialog.setOnClickedListener(object : CustomDialog.ButtonClickListener{
                override fun onClicked(candy: Int) {
                    val reqData = HashMap<String,Int>()
                    reqData["amount"] = candy
                    viewModel.updateCandyParent(CurrentUser.userToken!!,reqData)
                }
            })
        }

    }
}