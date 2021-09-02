package com.example.candy.challenge

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.candy.R
import com.example.candy.activity.ProblemActivity
import com.example.candy.challenge.viewmodel.LectureViewModel
import com.example.candy.databinding.ActivityChallengeLectureBinding
import com.example.candy.home.HomeViewModel
import com.example.candy.model.data.OnGoingChallenge
import com.example.candy.model.data.Problem
import com.example.candy.model.viewModel.SharedViewModel
import com.example.candy.utils.Util

class ChallengeLectureActivity : AppCompatActivity() {
    private val Tag = "ChallengeLectureActivity"
    private var _binding: ActivityChallengeLectureBinding? = null
    private val binding get() = _binding!!
    private lateinit var challenge: OnGoingChallenge
    private val lectureViewModel: LectureViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by lazy {
        ViewModelProvider(viewModelStore, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return when {
                    modelClass.isAssignableFrom(SharedViewModel::class.java) -> SharedViewModel.getInstance() as T
                    else -> throw IllegalArgumentException("Unknown viewModel class $modelClass")
                }
            }
        }).get(SharedViewModel::class.java)
    }
    private val homeViewModel: HomeViewModel by lazy {
        ViewModelProvider(viewModelStore, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return with(modelClass) {
                    when {
                        isAssignableFrom(HomeViewModel::class.java) -> HomeViewModel.getInstance()
                        else -> throw IllegalArgumentException("Unknown viewModel class $modelClass")
                    }
                } as T
            }
        }).get(HomeViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityChallengeLectureBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(findViewById(R.id.topBar))

        binding.titleBar.title.text = "챌린지 강의"
        binding.titleBar.backBtn.setOnClickListener {
            finish()
        }

        // intent를 통해 진행중인 챌린지의 정보 전달받음
        challenge = intent.getParcelableExtra("Challenge")!!

        // for test
        Util.toast(this, "test / challengeId : ${challenge.id}")

        initviews()
        initListeners()
    }

    private fun initListeners() {
        binding.btnGetCandy.setOnClickListener {
            if (challenge.requiredScore <= challenge.totalScore) {    // 점수 확인
                lectureViewModel.completeLecture(challenge = challenge).let {
                    if (it) {
                        sharedViewModel.assignCandyToStudent(challenge.assignedCandy)   // 캔디 획득
                        homeViewModel.removeOnGoingChallenge(challenge)                 // 진행중인 챌린지 리스트 수정
                        Util.toast(applicationContext, "챌린지 완료")
                        finish()
                    } else {
                        Util.toast(applicationContext, "챌린지 실패")
                    }
                }
            } else {
                Util.toast(applicationContext, "점수를 확인해 주세요")
                Log.d(Tag, "btnGetCandy.setOnClickListener / 점수가 낮음")
            }
        }

        binding.problemBtn.setOnClickListener {
            val intent= Intent(this,ProblemActivity::class.java)
            intent.putExtra("ChallengeId",challenge.id)
            startActivity(intent)
        }
    }

    private fun initviews() {
        // 챌린지 관련
        with(challenge) {
            binding.tvCategory.text = category
            binding.tvLevel.text = "2"      // ?
            binding.tvCandy.text = assignedCandy.toString()
            binding.tvRequiredScore.text = requiredScore.toString()
            binding.tvCurrentScore.text = totalScore.toString()
            binding.tvSubtitle.text = subTitle
        }

        // 썸네일 이미지
        lectureViewModel.getThombnailImage()

        // 임시 좋아요 아이콘
        Glide.with(binding.root).load(R.drawable.icon_challenge_like_empty)
            .into(binding.titleBar.favoriteIv)
    }
}