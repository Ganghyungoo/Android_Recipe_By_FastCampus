package com.test.youtubeappproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.test.youtubeappproject.adapter.VideoAdapter
import com.test.youtubeappproject.databinding.ActivityMainBinding
import com.test.youtubeappproject.dataclass.VideoItem
import com.test.youtubeappproject.dataclass.VideoList

class MainActivity : AppCompatActivity() {
    private val activityMainBinding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private lateinit var videoAdapter:VideoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activityMainBinding.root)

        videoAdapter = VideoAdapter(this){ videoItem ->
            activityMainBinding.motionLayout.setTransition(R.id.collapse, R.id.expand)
            activityMainBinding.motionLayout.transitionToEnd()
        }
        //우리가 설계할 떄 애초에 펼쳐진 상태를 기준으로 설계했으므로 닫힌상태로 설정한다.
        activityMainBinding.motionLayout.jumpToState(R.id.collapse)
        activityMainBinding.videoListRecyclerView.apply {
            adapter = videoAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }
        val videoList = readData("videos.json",VideoList::class.java) ?: VideoList(emptyList())
        Log.d("dataLog",videoList.toString())
        videoAdapter.submitList(videoList.videos)

    }
}