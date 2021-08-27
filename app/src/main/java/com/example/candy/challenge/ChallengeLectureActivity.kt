package com.example.candy.challenge

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.candy.R
import com.example.candy.data.ApiAnyResponse
import com.example.candy.databinding.ActivityChallengeLectureBinding
import com.example.candy.model.api.CandyApi
import com.example.candy.model.api.RetrofitClient
import com.example.candy.model.data.Challenge
import com.example.candy.utils.API.BASE_URL
import com.example.candy.utils.CurrentUser
import com.example.candy.utils.Util
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response

class ChallengeLectureActivity : AppCompatActivity() {

    private val Tag = "ChallengeLectureActivity"
    private var _binding: ActivityChallengeLectureBinding? = null
    private val binding get() = _binding!!
    private lateinit var challenge: Challenge

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityChallengeLectureBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(findViewById(R.id.topBar))

        binding.titleBar.title.text = "챌린지 강의"
        binding.titleBar.backBtn.setOnClickListener {
            finish()
        }

        challenge = intent.getParcelableExtra("Challenge")!!

        initviews()
        initListeners()
    }

    private fun initListeners() {
        binding.btnGetCandy.setOnClickListener{
            // 캔디 배정
            CoroutineScope(Dispatchers.IO).launch{
                val api = RetrofitClient.getClient(BASE_URL).create(CandyApi::class.java)
                val data = HashMap<String, Int>()
                data.put("challengeId",challenge.challengeId)
                val call = api.attainCandy(CurrentUser.userToken!!, data)

                call?.enqueue(object : retrofit2.Callback<ApiAnyResponse>{
                    override fun onResponse(
                        call: Call<ApiAnyResponse>,
                        response: Response<ApiAnyResponse>
                    ) {
                        if(response.isSuccessful){
                            Util.toast(applicationContext, "캔디 획득 성공")
                        }else{
                            Util.toast(applicationContext, "캔디 획득 실패")
                        }
                    }

                    override fun onFailure(call: Call<ApiAnyResponse>, t: Throwable) {

                        Util.toast(applicationContext, "attainCandy() error occurred")
                    }
                })
            }
            finish()
        }
    }

    private fun initviews(){
        // 챌린지 관련
        with(challenge){
            binding.tvCategory.text = category
            binding.tvLevel.text = "2"
            binding.tvCandy.text = "50"
            binding.tvRequiredScore.text = requiredScore.toString()
            binding.tvCurrentScore.text = totalScore.toString()
            binding.tvSubtitle.text = subTitle
        }

        // 임시 좋아요 아이콘
        Glide.with(binding.root).load(R.drawable.icon_challenge_like_empty)
            .into(binding.titleBar.favoriteIv)
    }
}