package com.example.candy.challenge

import android.os.Bundle
import android.os.Handler
import android.os.PersistableBundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.candy.R
import com.example.candy.challenge.viewmodel.ChallengeDetailViewModel
import com.example.candy.challenge.viewmodel.LikeChallengeViewModel
import com.example.candy.databinding.ActivityChallengeDetailBinding
import com.example.candy.model.injection.Injection


class ChallengeDetailActivity: AppCompatActivity() {

    private lateinit var binding: ActivityChallengeDetailBinding
    private lateinit var viewModel: ChallengeDetailViewModel
    private var challengeId = -1
    private var isAssigned = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("activity", "ChallengeDetail Activity onCreate")

        binding = DataBindingUtil.setContentView(this, R.layout.activity_challenge_detail)

        viewModel = ViewModelProvider(this, object: ViewModelProvider.Factory{
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return ChallengeDetailViewModel(
                        Injection.provideRepoRepository(applicationContext)
                ) as T
            }
        }).get(ChallengeDetailViewModel::class.java)

        // viewmodel data binding
        binding.viewmodel = viewModel

        // title bar
        binding.titleBar.title.text = "챌린지 소개"

        challengeId = intent.getIntExtra("challengeId", -1)

        Toast.makeText(this,"challengeId = ${challengeId}", Toast.LENGTH_SHORT).show()

        viewModel.category.observe(this,{
            binding.tvCategory.text = it
        })
        viewModel.title.observe(this,{
            binding.tvTitle.text = it
        })
        viewModel.subTitle.observe(this,{
            binding.tvSubTitle.text = it
        })
        viewModel.level.observe(this,{
            binding.tvLevel.text = it.toString()
        })
        viewModel.requiredScore.observe(this,{
            binding.tvRequiredScore.text = it.toString()
        })
        viewModel.description.observe(this,{
            binding.tvDescription.text = it
        })
        viewModel.challengeDetailProgressbar.observe(this,{
            if(it)
                binding.challengeDetailProgressbar.visibility = View.VISIBLE
            else
                binding.challengeDetailProgressbar.visibility = View.GONE
        })
        viewModel.assignedCandy.observe(this,{
            if(it > 0){
                isAssigned = true
            }
        })


        // 뒤로가기
        binding.titleBar.backBtn.setOnClickListener {
            onBackPressed()
        }


        // 정보 요청
       viewModel.getChallengeDetailInfo(challengeId)


        binding.tvCandyBtn.setOnClickListener {

            viewModel.getChallengeDetailInfo(challengeId)

            // 캔디배정 후 소개화면에서 캔디 배정 다시 누르면 다이얼로그 뜨지 않게 하기
            // 처음 다시 누르는 경우는 다이얼로그가 나타난다 -> 이 부분 해결하기

            if(isAssigned == false){
                var dialogView = CandyAssignDialogFragment()
                var bundle = Bundle()
                bundle.putInt("challengeId", challengeId)
                dialogView.arguments = bundle
                dialogView.show(supportFragmentManager, "candy assign dialog open")
            }
            else{
                Toast.makeText(this,"이미 캔디가 배정되었습니다", Toast.LENGTH_SHORT).show()
            }
        }

    }



}