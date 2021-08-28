package com.example.candy.challenge

import android.os.Bundle
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

        val challengeId = intent.getIntExtra("challengeId", -1)

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

        // 뒤로가기
        binding.titleBar.backBtn.setOnClickListener {
            onBackPressed()
        }


        // 정보 요청
        viewModel.getChallengeDetailInfo(challengeId)


        binding.tvCandyBtn.setOnClickListener {
            var dialogView = CandyAssignDialogFragment()
            dialogView.show(supportFragmentManager, "candy assign dialog open")
        }

    }


}