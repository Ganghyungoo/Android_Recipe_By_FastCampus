package com.test.youtubeappproject

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.test.youtubeappproject.adapter.PlayerVideoAdapter
import com.test.youtubeappproject.adapter.VideoAdapter
import com.test.youtubeappproject.databinding.ActivityMainBinding
import com.test.youtubeappproject.dataclass.VideoItem
import com.test.youtubeappproject.dataclass.VideoList

class MainActivity : AppCompatActivity() {
    private val activityMainBinding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private lateinit var videoAdapter: VideoAdapter
    private lateinit var playerVideoAdapter: PlayerVideoAdapter
    private val videoList:VideoList by lazy {
        readData("videos.json", VideoList::class.java) ?: VideoList(emptyList())
    }

    private var player: ExoPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activityMainBinding.root)

        //리사이클러뷰 어뎁터 초기화(아이템 터치리스너)
        initMainRecyclerViewAdapterAndItemTouch()
        initInnerRecyclerViewAdapterAndItemTouch()


        //모션 레이아웃 초기 설정
        initMotionLayout()

        //리사이클러뷰 어뎁터 연결
        initMainRecyclerView()
        initInnerRecyclerView()

        //데이터를 읽고 리사이클러뷰에 데이터 전달해서 업데이트
        readDataAndSubmitList()

        //비디오 플레이어 작아졌을 때의 컨트롤 버튼 설정
        initControllButton()


    }

    private fun initInnerRecyclerView() {
        activityMainBinding.playerRecyclerView.apply {
            adapter = playerVideoAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }
    }

    private fun initInnerRecyclerViewAdapterAndItemTouch() {
        playerVideoAdapter = PlayerVideoAdapter(this) { videoItem ->
            play(videoItem)
            val list = listOf<VideoItem>(videoItem) + videoList.videos.filter { it.id != videoItem.id }
            playerVideoAdapter.submitList(list)
        }
    }

    private fun initControllButton() {
        activityMainBinding.controlButton.setOnClickListener {
            player?.let {
                if (it.isPlaying) {
                    it.pause()
                } else {
                    it.play()
                }
            }
        }

        activityMainBinding.hideButton.setOnClickListener {
            activityMainBinding.motionLayout.transitionToState(R.id.hide)
            player?.stop()
        }
    }

    override fun onStart() {
        super.onStart()
        if (player == null) {
            initExoPlayer()
        }
    }

    override fun onStop() {
        super.onStop()
        player?.pause()
    }

    override fun onResume() {
        super.onResume()
        if (player == null) {
            initExoPlayer()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        player?.release()
        player = null
    }

    private fun initMainRecyclerViewAdapterAndItemTouch() {
        videoAdapter = VideoAdapter(this) { videoItem ->
            activityMainBinding.motionLayout.setTransition(R.id.collapse, R.id.expand)
            activityMainBinding.motionLayout.transitionToEnd()

            // 내가 누른 아이템을 상세 부분에 올려야하므로 필터링 작업 후 상세 화면의 어뎁터에 리스트 전달
            val list = listOf<VideoItem>(videoItem) + videoList.videos.filter { it.id != videoItem.id }
            playerVideoAdapter.submitList(list)
            play(videoItem)
        }
    }

    private fun initMainRecyclerView() {
        activityMainBinding.videoListRecyclerView.apply {
            adapter = videoAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }
    }

    private fun readDataAndSubmitList() {

        videoAdapter.submitList(videoList.videos)
    }

    private fun initMotionLayout() {
        activityMainBinding.motionLayout.targetView = activityMainBinding.playerView
        Log.d("testt", "현재 터치 타겟:${activityMainBinding.motionLayout.targetView} ")

        //우리가 설계할 떄 애초에 펼쳐진 상태를 기준으로 설계했으므로 닫힌상태로 설정한다.
        activityMainBinding.motionLayout.jumpToState(R.id.hide)

        activityMainBinding.motionLayout.setTransitionListener(object :
            MotionLayout.TransitionListener {
            override fun onTransitionStarted(
                motionLayout: MotionLayout?,
                startId: Int,
                endId: Int,
            ) {
                //상태 변화 시작(implements중)
            }

            override fun onTransitionChange(
                motionLayout: MotionLayout?,
                startId: Int,
                endId: Int,
                progress: Float,
            ) {
                activityMainBinding.playerView.useController = false
            }

            override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {
                activityMainBinding.playerView.useController = (currentId == R.id.expand)
            }

            override fun onTransitionTrigger(
                motionLayout: MotionLayout?,
                triggerId: Int,
                positive: Boolean,
                progress: Float,
            ) {
                TODO("Not yet implemented")
            }

        })


    }

    private fun play(videoItem: VideoItem) {
        player?.setMediaItem(MediaItem.fromUri(Uri.parse(videoItem.videoUrl)))
        player?.prepare()
        player?.play()

        activityMainBinding.videoTitleTextView.text = videoItem.title
    }

    fun initExoPlayer() {
        player = ExoPlayer.Builder(this).build()
            .also { exoPlayer ->
                activityMainBinding.playerView.player = exoPlayer
                activityMainBinding.playerView.useController = false
                exoPlayer.addListener(object : Player.Listener {
                    override fun onIsPlayingChanged(isPlaying: Boolean) {
                        super.onIsPlayingChanged(isPlaying)

                        if (isPlaying) {
                            activityMainBinding.controlButton.setImageResource(R.drawable.baseline_pause_24)
                        } else {
                            activityMainBinding.controlButton.setImageResource(R.drawable.baseline_play_arrow_24)
                        }
                    }
                })
            }
    }

}