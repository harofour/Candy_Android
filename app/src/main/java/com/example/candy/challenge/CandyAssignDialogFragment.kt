package com.example.candy.challenge

import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.databinding.library.baseAdapters.BR
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.candy.R
import com.example.candy.challenge.viewmodel.ChallengeDetailViewModel
import com.example.candy.challenge.viewmodel.PossibleChallengeViewModel
import com.example.candy.databinding.FragmentDialogCandyAssignBinding
import com.example.candy.model.injection.Injection

class CandyAssignDialogFragment: DialogFragment() {

    private lateinit var binding : FragmentDialogCandyAssignBinding
    private lateinit var viewModel: ChallengeDetailViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_dialog_candy_assign, container, false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)

        // 다이얼로그 외부 터치 시 사라짐 방지 / close icon을 통해 닫을 수 있음
        /*dialog!!.setOnShowListener(object : DialogInterface.OnShowListener{
            override fun onShow(p0: DialogInterface?) {
                dialog!!.setCancelable(false)
            }
        }) */

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentDialogCandyAssignBinding.bind(view)
        viewModel = ViewModelProvider(viewModelStore, object: ViewModelProvider.Factory{
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return ChallengeDetailViewModel(
                        Injection.provideRepoRepository(context!!)
                ) as T
            }
        }).get(ChallengeDetailViewModel::class.java)

        binding.setVariable(BR.viewmodel, viewModel)


        //다이얼로그 크기 설정
        // 스크린 크기
        var wm : WindowManager = getContext()?.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        var screen : Display = wm.getDefaultDisplay()

        var size = Point()
        screen.getRealSize(size)
        var height = size.y
        var width = size.x

        //다이얼로그 크기
        binding.dialogLayout.layoutParams.width = width * 311 / 375
        binding.dialogLayout.layoutParams.height = width * 417 / 375

    }




}