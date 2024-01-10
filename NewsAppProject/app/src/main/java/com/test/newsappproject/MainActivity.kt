package com.test.newsappproject

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.test.newsappproject.adapter.NewsAdapter
import com.test.newsappproject.dataModel.NewsRss
import com.test.newsappproject.dataModel.changeToModel
import com.test.newsappproject.databinding.ActivityMainBinding
import com.tickaroo.tikxml.TikXml
import com.tickaroo.tikxml.retrofit.TikXmlConverterFactory
import org.jsoup.HttpStatusException
import org.jsoup.Jsoup
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

        //TODO 레트로핏 통신으로 메서드로 뺄예정
        val newService = retrofit.create(NewService::class.java)

        newsAdater = NewsAdapter()
        val linearLayoutManager = LinearLayoutManager(this)

        activityMainBinding.newsRecyclerView.apply {
            adapter = newsAdater
            layoutManager = linearLayoutManager
        }


        activityMainBinding.run{
            defaultChip.isChecked = true
            newService.mainFeed().submitList()

            defaultChip.setOnCloseIconClickListener {
                if (chipGroup.checkedChipId == defaultChip.id){
                    return@setOnCloseIconClickListener
                }
                chipGroup.clearCheck()
                defaultChip.isChecked = true
                newService.mainFeed().submitList()
            }
            politicsChip.setOnClickListener {
                chipGroup.clearCheck()
                politicsChip.isChecked = true
                newService.politicsNews().submitList()
            }
            economyChip.setOnClickListener {
                chipGroup.clearCheck()
                economyChip.isChecked = true
                newService.economyNews().submitList()
            }
            societyChip.setOnClickListener {
                chipGroup.clearCheck()
                societyChip.isChecked = true
                newService.societyNews().submitList()
            }
            internationalChip.setOnClickListener {
                chipGroup.clearCheck()
                internationalChip.isChecked = true
                newService.internationalNews().submitList()
            }
            cultureChip.setOnClickListener {
                chipGroup.clearCheck()
                cultureChip.isChecked = true
                newService.cultureNews().submitList()
            }
            enterChip.setOnClickListener {
                chipGroup.clearCheck()
                enterChip.isChecked = true
                newService.enterNews().submitList()
            }
            sportsChip.setOnClickListener {
                chipGroup.clearCheck()
                sportsChip.isChecked = true
                newService.sportsNews().submitList()
            }
            healthChip.setOnClickListener {
                chipGroup.clearCheck()
                healthChip.isChecked = true
                newService.healthNews().submitList()
            }
        }
    }

   private fun Call<NewsRss>.submitList() {
       enqueue(object : Callback<NewsRss> {
           override fun onResponse(call: Call<NewsRss>, response: Response<NewsRss>) {
               Log.d("MainNetwork", "${response.body()?.channel?.items}")

               //얻어온 기사 리스트에서 사진을 끌어오기 위해 해당 리스트 아이템의 링크에 접근해서 meta image를 추출하는 작업
               val list = response.body()?.channel?.items.orEmpty().changeToModel()
               newsAdater.submitList(list)
               list.forEachIndexed { idx, item ->
                   Thread{
                       try {
                           val jsoup = Jsoup.connect(item.link).get()
                           val elements = jsoup.select("meta[property^=og:]")
                           val ogImageNode = elements.find {
                               it.attr("property") == "og:image"
                           }
                           item.imageUrl = ogImageNode?.attr("content")
                       }catch (e: HttpStatusException){
                           item.imageUrl = null
                       }
                       runOnUiThread {
                           newsAdater.notifyItemChanged(idx)
                       }
                       Log.d("getiImage", "$item.imageUrl")
                   }.start()
               }


           }

           override fun onFailure(call: Call<NewsRss>, t: Throwable) {
               //오류가 날 경우
           }
       })
    }
}