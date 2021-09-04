package com.example.candy.challenge

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.candy.R
import com.example.candy.activity.ProblemActivity
import com.example.candy.challenge.viewmodel.LectureViewModel
import com.example.candy.databinding.ActivityChallengeLectureBinding
import com.example.candy.home.HomeViewModel
import com.example.candy.home.ParentPasswordCheckDialogFragment
import com.example.candy.model.data.OnGoingChallenge
import com.example.candy.model.viewModel.SharedViewModel
import com.example.candy.utils.API.BASE_URL
import com.example.candy.utils.CurrentUser
import com.example.candy.utils.DIALOG_REQUEST_KEY
import com.example.candy.utils.Util
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.audio.AudioAttributes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.properties.Delegates


class ChallengeLectureActivity : AppCompatActivity() {
    private val Tag = "ChallengeLectureActivity"
    private var _binding: ActivityChallengeLectureBinding? = null
    private val binding get() = _binding!!

    private var uri: Uri = Uri.parse("${BASE_URL}/challenge/video/lecture/view?video_url=")
    private lateinit var lecturePlayer: SimpleExoPlayer

    private var scoredScore by Delegates.notNull<Int>()
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
//        Util.toast(
//            this,
//            "test / challengeId: ${challenge.challengeId}\n lectureId : ${challenge.lecturesId}"
//        )

        initviews()
        initListeners()
        initScoreData()
    }

    private fun initScoreData() {
        sharedViewModel.getScoredScore().observe(this,{
            scoredScore = it
            binding.tvCurrentScore.text = it.toString()
        })
    }

    override fun onResume() {
        super.onResume()
        sharedViewModel.getAPIScoredScore(CurrentUser.userToken!!,challenge.challengeId)
    }

    private fun initListeners() {
        binding.btnGetCandy.setOnClickListener {
            if (challenge.requiredScore <= scoredScore) {    // 점수 확인
                lectureViewModel.completeLecture(challenge = challenge).let {
                    sharedViewModel.assignCandyToStudent(challenge.assignedCandy)   // 캔디 획득
                    finish()
                }

            } else {
                Util.toast(applicationContext, "점수를 확인해 주세요")
                Log.d(Tag, "btnGetCandy.setOnClickListener / 점수가 낮음")
            }
        }

        binding.problemBtn.setOnClickListener {
            val intent = Intent(this, ProblemActivity::class.java)
            intent.putExtra("ChallengeId", challenge.challengeId)
            startActivity(intent)
        }
        binding.btnCancelAssignCandy.setOnClickListener {
            getParentPassword()
        }
    }

    private fun getParentPassword() {
        // DialogFragment.show() will take care of adding the fragment
        // in a transaction.  We also want to remove any currently showing
        // dialog, so make our own transaction and take care of that here.
        val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
        val prev: Fragment? = supportFragmentManager.findFragmentByTag("dialog")
        if (prev != null) {
            ft.remove(prev)
        }
        ft.addToBackStack(null)

        // Create and show the dialog.
        val dialog = ParentPasswordCheckDialogFragment()
        dialog.show(supportFragmentManager, "dialog")

        dialog.setFragmentResultListener(DIALOG_REQUEST_KEY) { reqKey, bundle ->
            if (DIALOG_REQUEST_KEY == reqKey) {
                val parentPassword = bundle.getString("ParentPassword")

                parentPassword?.let {
                    val reqData = HashMap<String, Any>()
                    reqData.put("challengeId", challenge.challengeId)
                    reqData.put("parentPassword", parentPassword)

                    CoroutineScope(Dispatchers.IO).launch {
                        if (homeViewModel.cancelAssignedCandy(reqData)) {
                            Util.toast(applicationContext, "캔디 배정이 취소되었습니다")
                            finish()
                        } else {
                            Util.toast(applicationContext, "캔디 배정 취소를 실패했습니다")
                        }
                    }
                }
            }
        }
    }

    private fun initviews() {
        // 강의 동영상
        // audio focus
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(C.USAGE_MEDIA)
            .setContentType(C.CONTENT_TYPE_MOVIE)
            .build()
        // init player
        lecturePlayer = SimpleExoPlayer.Builder(this).build()
        lecturePlayer.setAudioAttributes(audioAttributes, true)
        binding.playerView.player = lecturePlayer
        // get uri and set player
        CoroutineScope(Dispatchers.IO).launch {
            lectureViewModel.loadVideo(challenge.challengeId, challenge.lecturesId!![0])
                ?.also { response ->
                    CoroutineScope(Dispatchers.Main).launch {
                        val mediaItem = MediaItem.fromUri("$uri$response")
                        lecturePlayer.setMediaItem(mediaItem)
                        lecturePlayer.prepare()
                        lecturePlayer.play()
                    }
                }
        }

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

    override fun onPause() {
        super.onPause()

        // 앱 포커스를 잃을 때 pause
        lecturePlayer.pause()
    }
}