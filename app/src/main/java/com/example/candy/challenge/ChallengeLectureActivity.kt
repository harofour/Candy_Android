package com.example.candy.challenge

import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
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
    private var isFullScreen = false        // play screen size flag

    private val fullScreenBtn by lazy { findViewById<ImageView>(R.id.exo_fullscreen_icon) } // player fullscreen button

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
                return when {
                    modelClass.isAssignableFrom(HomeViewModel::class.java) -> HomeViewModel.getInstance()
                    else -> throw IllegalArgumentException("Unknown viewModel class $modelClass")
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

        initViews()
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

    private fun initViews() {
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
            lectureViewModel.loadVideo(challenge.challengeId, challenge.lecturesId)
                ?.also { response ->
                    response.takeIf {
                        it.contains(".")    // 동영상이 맞는지 확인하는 조건. .mp4 등을 포함하므로 우선 '.'이 있으면 플레이어 초기화
                    }?.apply {
                        CoroutineScope(Dispatchers.Main).launch {
                            val mediaItem = MediaItem.fromUri("$uri$response")
                            lecturePlayer.setMediaItem(mediaItem)
                            lecturePlayer.prepare()
                            lecturePlayer.play()
                        }
                    }
                }
        }

        // 챌린지 관련
        with(challenge) {
            binding.tvCategory.text = category
            binding.tvLevel.text = level.toString()
            binding.tvCandy.text = assignedCandy.toString()
            binding.tvRequiredScore.text = requiredScore.toString()
            binding.tvCurrentScore.text = totalScore.toString()
            binding.tvSubtitle.text = subTitle
        }

        // 썸네일 이미지
        lectureViewModel.getThombnailImage()
    }

    private fun initListeners() {
        // 캔디 획득
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

        // 캔디 배정 취소
        binding.btnCancelAssignCandy.setOnClickListener {
            getParentPassword()
        }

        // 문제 풀기로 이동
        binding.problemBtn.setOnClickListener {
            val intent = Intent(this, ProblemActivity::class.java)
            intent.putExtra("ChallengeId", challenge.challengeId)
            startActivity(intent)
        }

        // player fullscreen button
        fullScreenBtn.setOnClickListener {
            playerSizeChanger()
        }
    }

    /**
     * 배정 취소를 위해 다이얼로그를 띄어 학부모 비밀번호를 확인 후 처리
     */
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
        // dialog를 통해 패스워드를 입력받음
        val dialog = ParentPasswordCheckDialogFragment()
        dialog.show(supportFragmentManager, "dialog")

        dialog.setFragmentResultListener(DIALOG_REQUEST_KEY) { reqKey, bundle ->
            if (DIALOG_REQUEST_KEY == reqKey) {
                // 입력받은 학부모 비밀번호
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

    /**
     * change exoplayer screen size to Full or Origin size
     */
    private fun playerSizeChanger() {
        if (isFullScreen) {
            fullScreenBtn.setImageDrawable(
                ContextCompat.getDrawable(this, R.drawable.ic_fullscreen_open)
            )

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
            } else {
                window.setDecorFitsSystemWindows(true)
                window.insetsController?.show(
                    WindowInsets.Type.navigationBars()
                            or WindowInsets.Type.statusBars()
                )
            }

            if (supportActionBar != null) {
                supportActionBar!!.show()
            }

            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            val params = binding.playerView.layoutParams
            params.width = 0
            params.height = 0
            binding.playerView.layoutParams = params
            isFullScreen = false
        } else {
            fullScreenBtn.setImageDrawable(
                ContextCompat.getDrawable(this, R.drawable.ic_fullscreen_close)
            )

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
                window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
            } else {
                window.setDecorFitsSystemWindows(false)
                window.insetsController?.hide(
                    WindowInsets.Type.navigationBars()
                            or WindowInsets.Type.statusBars()
                )
            }

            if (supportActionBar != null) {
                supportActionBar!!.hide()
            }

            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            val params = binding.playerView.layoutParams
            params.width = ViewGroup.LayoutParams.MATCH_PARENT
            params.height = ViewGroup.LayoutParams.MATCH_PARENT
            binding.playerView.layoutParams = params
            isFullScreen = true
        }
    }

    override fun onBackPressed() {
        // back 버튼 눌렀을 때 player가 fullscreen 이면 원래 사이즈로 돌아오도록 함
        if (isFullScreen) {
            playerSizeChanger()
        } else {
            super.onBackPressed()
        }
    }

    override fun onPause() {
        super.onPause()

        // 앱 포커스를 잃을 때 player pause
        lecturePlayer.pause()
    }
}