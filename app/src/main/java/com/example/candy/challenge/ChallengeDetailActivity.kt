package com.example.candy.challenge

import android.net.Uri
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.candy.R
import com.example.candy.challenge.viewmodel.ChallengeDetailViewModel
import com.example.candy.databinding.ActivityChallengeDetailBinding
import com.example.candy.model.injection.Injection
import com.example.candy.utils.API
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory


class ChallengeDetailActivity: AppCompatActivity() {

    private lateinit var binding: ActivityChallengeDetailBinding
    private lateinit var viewModel: ChallengeDetailViewModel
    private var challengeId = -1
    private var lectureId = -100
    private var isAssigned = false

    private var IS_COMPLETE : Boolean = false

    private var baseUrl ="${API.BASE_URL}challenge/video/lecture/view?video_url="
    private var player: SimpleExoPlayer? = null
    private val listener = PlayerStateListener()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("activity", "ChallengeDetail Activity onCreate")

        binding = DataBindingUtil.setContentView(this, R.layout.activity_challenge_detail)

        viewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return ChallengeDetailViewModel(
                        Injection.provideRepoRepositoryRx(applicationContext)
                ) as T
            }
        }).get(ChallengeDetailViewModel::class.java)

        // viewmodel data binding
        binding.viewmodel = viewModel

        // title bar
        binding.titleBar.title.text = "챌린지 소개"

        // 리스트에서 선택한 챌린지 아이디 전달 받기
        challengeId = intent.getIntExtra("challengeId", -1)

        // lectureId 전달받기 / api 수정되면 작업 이어서 하기
        lectureId = intent.getIntExtra("lectureId", -1)

        // 완료된 챌린지 여부 전달 받기
        IS_COMPLETE = intent.getBooleanExtra("isCompleted", false)

        Log.d("api test check", "challengeDetail onCreate challengeId : ${challengeId}")
        Log.d("api test check", "challengeDetail onCreate lectureId : ${lectureId}")
        Log.d("api test check", "challengeDetail onCreate isCompleted : ${IS_COMPLETE}")



        //Toast.makeText(this, "challengeId = ${challengeId}", Toast.LENGTH_SHORT).show()


        viewModel.category.observe(this, {
            binding.tvCategory.text = it
        })
        viewModel.title.observe(this, {
            binding.tvTitle.text = it
        })
        viewModel.subTitle.observe(this, {
            binding.tvSubTitle.text = it
        })
        viewModel.level.observe(this, {
            binding.tvLevel.text = it.toString()
        })
        viewModel.requiredScore.observe(this, {
            binding.tvRequiredScore.text = it.toString()
        })
        viewModel.description.observe(this, {
            binding.tvDescription.text = it
        })
        viewModel.challengeDetailProgressbar.observe(this, {
            if (it)
                binding.challengeDetailProgressbar.visibility = View.VISIBLE
            else
                binding.challengeDetailProgressbar.visibility = View.GONE
        })
       /* viewModel.assignedCandyCount.observe(this,{
            if(it > 0){
                isAssigned = true
            }
        }) */

        // 강의 영상 로딩하는 동안 progressbar
        viewModel.challengeDetailVideoLoadProgressbar.observe(this, {
            if (it)
                binding.challengeDetailProgressbarVideo.visibility = View.VISIBLE
            else
                binding.challengeDetailProgressbarVideo.visibility = View.GONE
        })


        // 뒤로가기
        binding.titleBar.backBtn.setOnClickListener {
            onBackPressed()
        }


        // 강의 영상 미리보기 로드
        initVideoPlayer(binding)

        // 정보 요청
       viewModel.getChallengeDetailInfo(challengeId)

        binding.tvCandyBtn.setOnClickListener {

            if(IS_COMPLETE == false) {
                var dialogView = CandyAssignDialogFragment()
                var bundle = Bundle()
                bundle.putInt("challengeId", challengeId)
                dialogView.arguments = bundle
                dialogView.show(supportFragmentManager, "candy assign dialog open")
            }
            else
                Toast.makeText(applicationContext, "완료된 챌린지 입니다", Toast.LENGTH_SHORT).show()
        }

        Log.d("api test check", "video duration2 : ${player!!.duration}")



    }

    private fun initVideoPlayer(binding: ActivityChallengeDetailBinding){
        player = SimpleExoPlayer.Builder(this).build()
        binding.playerView.player = player

        // 비디오 로드
        viewModel.videoUrl.observe(this, {
            it?.let {
                var responseUrl = it  // 반환 받은 url

                var finalVideoUrl = baseUrl + responseUrl

                val dataSourceFactory = DefaultDataSourceFactory(this)
                val mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
                        .createMediaSource(MediaItem.fromUri(Uri.parse(finalVideoUrl)))

                player!!.setMediaSource(mediaSource)

                if(IS_COMPLETE == false){  // 미완료 챌린지인 경우
                    player!!.addListener(listener)

                    player!!.createMessage ( {messageType: Int, payload: Any? ->

                        Log.d("api test check", "time check 60000")
                        player!!.pause()

                    }).setLooper(Looper.getMainLooper())
                            .setPosition(60000)  // 5초 = 5000   // 우선 5초되면 자동으로 정지된다
                            .setDeleteAfterDelivery(false)
                            .send()
                }

                player!!.prepare()

                //player!!.play()

                Log.d("api test check", "video duration : ${player!!.duration}")
                Log.d("api test check", "video content duration : ${player!!.contentDuration}")
                Log.d("api test check", "video content currentTimeline : ${player!!.currentTimeline}")

            }
        })

        // 영상 url 가져오기 호출
        viewModel.getVideoUrl(challengeId, lectureId)


    }

    override fun onStop() {
        super.onStop()

        player?.pause()
    }

    override fun onDestroy() {
        super.onDestroy()

        player?.release()

    }


    inner class PlayerStateListener : Player.EventListener {
        override fun onPlaybackStateChanged(state: Int) {
            when (state) {
                ExoPlayer.STATE_IDLE -> Log.d("exo check", "STATE_IDLE")
                ExoPlayer.STATE_BUFFERING -> Log.d("exo check", "STATE_BUFFERING")
                ExoPlayer.STATE_READY -> {
                    Log.d("exo check", "STATE_READY")
                    Log.d("api test check", "video duration : ${player!!.duration}")
                }

                ExoPlayer.STATE_ENDED -> Log.d("exo check", "STATE_ENDED")

            }
        }

        override fun onIsPlayingChanged(isPlaying: Boolean) {
            super.onIsPlayingChanged(isPlaying)


            // 상태가 변할 시 호출  정지->재생 or 재생 -> 정지 일때 현재 위치가 5초 이상이면 처음으로 되돌아 간다
            if(isPlaying){
                Log.d("video check", "playing")
                if(player!!.currentPosition >= 60000){
                    player!!.seekTo(0)
                    Toast.makeText(applicationContext,"미리보기 강의는 1분까지 시청 가능합니다", Toast.LENGTH_SHORT).show()
                }
            }
            else{
                Log.d("video check", "not playing / current position ${player!!.currentPosition}")
                if(player!!.currentPosition >= 60000){
                    player!!.seekTo(0)
                    Toast.makeText(applicationContext,"미리보기 강의는 1분까지 시청 가능합니다", Toast.LENGTH_SHORT).show()
                }
            }
        }


    }


}