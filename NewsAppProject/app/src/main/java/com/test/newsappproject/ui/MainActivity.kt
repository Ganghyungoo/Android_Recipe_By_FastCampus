package com.test.newsappproject.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.test.newsappproject.adapter.NewsAdapter
import com.test.newsappproject.dataModel.NewsRss
import com.test.newsappproject.dataModel.changeToModel
import com.test.newsappproject.databinding.ActivityMainBinding
import com.test.newsappproject.`object`.APIClient
import org.jsoup.HttpStatusException
import org.jsoup.Jsoup
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

//MBN뉴스 가져오기
class MainActivity : AppCompatActivity() {
    private lateinit var activityMainBinding: ActivityMainBinding
    private lateinit var newsAdater: NewsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)

        //리사이클러 뷰 설정
        newsAdater = NewsAdapter{
            val intent = Intent(this,WebViewActivity::class.java)
            intent.putExtra("url",it)
            startActivity(intent)
        }
        val linearLayoutManager = LinearLayoutManager(this)

        activityMainBinding.newsRecyclerView.apply {
            adapter = newsAdater
            layoutManager = linearLayoutManager
        }


        //chip 선택에 따른 레트로핏 통신 분기
        activityMainBinding.run{
            defaultChip.isChecked = true
            APIClient.newService.mainFeed().submitList()

            defaultChip.setOnCloseIconClickListener {
                if (chipGroup.checkedChipId == defaultChip.id){
                    return@setOnCloseIconClickListener
                }
                chipGroup.clearCheck()
                defaultChip.isChecked = true
                APIClient.newService.mainFeed().submitList()
            }
            politicsChip.setOnClickListener {
                chipGroup.clearCheck()
                politicsChip.isChecked = true
                APIClient.newService.politicsNews().submitList()
            }
            economyChip.setOnClickListener {
                chipGroup.clearCheck()
                economyChip.isChecked = true
                APIClient.newService.economyNews().submitList()
            }
            societyChip.setOnClickListener {
                chipGroup.clearCheck()
                societyChip.isChecked = true
                APIClient.newService.societyNews().submitList()
            }
            internationalChip.setOnClickListener {
                chipGroup.clearCheck()
                internationalChip.isChecked = true
                APIClient.newService.internationalNews().submitList()
            }
            cultureChip.setOnClickListener {
                chipGroup.clearCheck()
                cultureChip.isChecked = true
                APIClient.newService.cultureNews().submitList()
            }
            enterChip.setOnClickListener {
                chipGroup.clearCheck()
                enterChip.isChecked = true
                APIClient.newService.enterNews().submitList()
            }
            sportsChip.setOnClickListener {
                chipGroup.clearCheck()
                sportsChip.isChecked = true
                APIClient.newService.sportsNews().submitList()
            }
            healthChip.setOnClickListener {
                chipGroup.clearCheck()
                healthChip.isChecked = true
                APIClient.newService.healthNews().submitList()
            }
        }
    }

    //레트로핏 통신에 대한 확장함수 선언
   private fun Call<NewsRss>.submitList() {
       enqueue(object : Callback<NewsRss> {
           override fun onResponse(call: Call<NewsRss>, response: Response<NewsRss>) {
               Log.d("MainNetwork", "${response.body()?.channel?.items}")

               //얻어온 기사 리스트에서 사진을 끌어오기 위해 해당 리스트 아이템의 링크에 접근해서 meta image를 추출하는 작업
               val list = response.body()?.channel?.items.orEmpty().changeToModel()
               newsAdater.submitList(list)

               activityMainBinding.notFoundView.isVisible = list.isEmpty()

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