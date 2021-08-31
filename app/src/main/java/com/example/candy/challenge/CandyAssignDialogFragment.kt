package com.example.candy.challenge


import android.content.Context
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.library.baseAdapters.BR
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.candy.R
import com.example.candy.challenge.viewmodel.ChallengeDetailViewModel
import com.example.candy.databinding.FragmentDialogCandyAssignBinding
import com.example.candy.model.injection.Injection

class CandyAssignDialogFragment: DialogFragment() {

    private lateinit var binding : FragmentDialogCandyAssignBinding
    private lateinit var viewModel: ChallengeDetailViewModel
    private var Challenge_Id: Int = -1000

    private var CurrentCandy = 0
    private var isAssign = false

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

        var bundle = arguments
         Challenge_Id = bundle!!.getInt("challengeId", Challenge_Id)

        Toast.makeText(context,"challengeId when dialog open : ${Challenge_Id}", Toast.LENGTH_SHORT).show()

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("dialog test", "onViewCreated called")

        binding = FragmentDialogCandyAssignBinding.bind(view)
        viewModel = ViewModelProvider(viewModelStore, object: ViewModelProvider.Factory{
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return ChallengeDetailViewModel(
                        Injection.provideRepoRepositoryRx(context!!)
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
        binding.dialogLayout.layoutParams.width = width * 310 / 375
        binding.dialogLayout.layoutParams.height = width * 415 / 375


        var tv_current_candy = view.findViewById<TextView>(R.id.tv_parent_candy)  // 바인딩 인식이 안되서 우선 findViewById 사용

        viewModel.candyAmount.observe(viewLifecycleOwner, {
            tv_current_candy.text = it.toString()
            CurrentCandy = it
        })

        // 현재 학부모 보유 캔디 조회
        viewModel.getParentCandy()

        // progressbar observe
        var progressbar = view.findViewById<ProgressBar>(R.id.dialog_progressbar)
        viewModel.dialogProgressBarVisible.observe(viewLifecycleOwner, {
            if(it){
                progressbar.visibility = View.VISIBLE
            }
            else{
                progressbar.visibility = View.GONE
            }
        })

        viewModel.assignSuccess.observe(viewLifecycleOwner, {
            if(it){
                closeDialog()
                Toast.makeText(context,"캔디 배정에 성공했습니다", Toast.LENGTH_SHORT).show()
                isAssign = true
            }
        })



        //var assignBtnTv = view.findViewById<TextView>(R.id.tv_candy_assign)
        var assignBtnLinear = view.findViewById<LinearLayout>(R.id.linearlayout_candy_assign)
        var et_candy_put = view.findViewById<EditText>(R.id.et_assign_candy)

        assignBtnLinear.setOnClickListener {
            if (isAssign) {
                Toast.makeText(context, "이미 캔디가 배정된 챌린지입니다", Toast.LENGTH_SHORT).show()
            } else {
                var et_candy = et_candy_put.text.toString()
                if (et_candy.length == 0) {
                    Toast.makeText(context, "배정할 캔디 개수를 입력하세요", Toast.LENGTH_SHORT).show()
                } else {
                    var putCandy = et_candy.toInt()
                    if (putCandy > CurrentCandy)
                        Toast.makeText(context, "입력 캔디 개수가 현재 보유 캔디보다 많습니다", Toast.LENGTH_SHORT)
                            .show()
                    else {
                       viewModel.assignCandy(Challenge_Id, putCandy)
                        Log.d("api test", "candy cnt ${putCandy}")
                    }
                }
            }
        }
    }


    //다이얼로그 닫기
    fun closeDialog(){
        val fragmentManager = (activity as AppCompatActivity).supportFragmentManager
        fragmentManager.beginTransaction().remove(this).commit();
        fragmentManager.popBackStack();
    }




}