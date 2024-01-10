package com.test.githubreposearchproject

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.test.githubreposearchproject.adapter.RepoAdapter
import com.test.githubreposearchproject.databinding.ActivityRepoBinding
import com.test.githubreposearchproject.datamodel.Repo
import com.test.githubreposearchproject.network.GithubService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RepoActivity : AppCompatActivity() {
    private lateinit var activityRepoBinding: ActivityRepoBinding
    private lateinit var repoAdapter: RepoAdapter
    private var searchPage = 0
    private var repoName = ""
    private var hasMore = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityRepoBinding = ActivityRepoBinding.inflate(layoutInflater)
        setContentView(activityRepoBinding.root)

        val userName = intent.getStringExtra("userName") ?: return

        repoAdapter = RepoAdapter{
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it.htmlUrl))
            startActivity(intent)
        }

        //통신(검색) 메서드 실행
        repoSearch(userName, searchPage)

        activityRepoBinding.userNameTextView.text = userName

        //리사이클러뷰 설정
        activityRepoBinding.repoRecyclerView.apply {
            val linearLayoutManager = LinearLayoutManager(this@RepoActivity)
            layoutManager = linearLayoutManager
            adapter = repoAdapter

            //마지막 아이템까지 스크롤 시 통신 page + 1
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    val recyclerViewTotalCount = linearLayoutManager.itemCount
                    val lastVisiblePosition = linearLayoutManager.findLastVisibleItemPosition()
                    Log.d("scroll","""현재 검색된 아이템 수 :$recyclerViewTotalCount 
                        |지금 스크롤해서 보이는 마지막 아이템 번쨰:$lastVisiblePosition""".trimMargin())

                    if (lastVisiblePosition >= recyclerViewTotalCount - 1 && hasMore){
                        searchPage++
                        repoSearch(userName,searchPage)
                    }

                }
            })
        }
    }

    private fun repoSearch(userName: String, page:Int) {
        val githubService = APIClient.retrofit.create(GithubService::class.java)
        githubService.listRepos(userName, page/*,30*/).enqueue(object : Callback<List<Repo>> {
            override fun onResponse(call: Call<List<Repo>>, response: Response<List<Repo>>) {
                Log.d("testt", "List Repo: ${response.body().toString()}")

                hasMore = response.body()?.count() == 30
                repoAdapter.submitList(repoAdapter.currentList + response.body().orEmpty())
            }

            override fun onFailure(call: Call<List<Repo>>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }
}