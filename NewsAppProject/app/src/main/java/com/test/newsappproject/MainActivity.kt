package com.test.newsappproject

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.test.newsappproject.adapter.NewsAdapter
import com.test.newsappproject.dataModel.NewsRss
import com.test.newsappproject.databinding.ActivityMainBinding
import com.tickaroo.tikxml.TikXml
import com.tickaroo.tikxml.retrofit.TikXmlConverterFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

//MBN뉴스 가져오기
class MainActivity : AppCompatActivity() {
    private lateinit var activityMainBinding: ActivityMainBinding
    private lateinit var newsAdater: NewsAdapter

    //TODO 레트로핏 선언 (나중에 object형태로 따로 뺄 예정)
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://www.mbn.co.kr/")
        //xml 형식 변환
        .addConverterFactory(
            TikXmlConverterFactory.create(
                TikXml.Builder()
                    .exceptionOnUnreadXml(false)
                    .build()
            )
        ).build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)

        newsAdater = NewsAdapter()
        val linearLayoutManager = LinearLayoutManager(this)

        activityMainBinding.newsRecyclerView.apply {
            adapter = newsAdater
            layoutManager = linearLayoutManager
        }


        //TODO 레트로핏 통신으로 메서드로 뺄예정
        val newService = retrofit.create(NewService::class.java)
        newService.mainFeed().enqueue(object : Callback<NewsRss> {
            override fun onResponse(call: Call<NewsRss>, response: Response<NewsRss>) {
                newsAdater.submitList(response.body()?.channel?.items.orEmpty())
                Log.d("MainNetwork", "${response.body()?.channel?.items}")
            }

            override fun onFailure(call: Call<NewsRss>, t: Throwable) {
                //오류가 날 경우
            }

        })

    }
}