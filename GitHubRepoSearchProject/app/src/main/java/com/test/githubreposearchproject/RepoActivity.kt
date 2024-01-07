package com.test.githubreposearchproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.test.githubreposearchproject.adapter.RepoAdapter
import com.test.githubreposearchproject.databinding.ActivityRepoBinding
import com.test.githubreposearchproject.datamodel.Repo
import com.test.githubreposearchproject.network.GithubService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RepoActivity : AppCompatActivity() {
    private lateinit var activityRepoBinding: ActivityRepoBinding
    private lateinit var repoAdapter: RepoAdapter
    private var repoName = ""

    private val retrofit = Retrofit.Builder()
        .baseUrl(" https://api.github.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityRepoBinding = ActivityRepoBinding.inflate(layoutInflater)
        setContentView(activityRepoBinding.root)

        val userName = intent.getStringExtra("userName") ?: return

        repoAdapter = RepoAdapter()
        repoSearch(userName)

        activityRepoBinding.userNameTextView.text = userName

        activityRepoBinding.repoRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@RepoActivity)
            adapter = repoAdapter
        }
    }

    private fun repoSearch(userName: String) {

        val githubService = retrofit.create(GithubService::class.java)
        githubService.listRepos(userName).enqueue(object : Callback<List<Repo>> {
            override fun onResponse(call: Call<List<Repo>>, response: Response<List<Repo>>) {
                Log.d("testt", "List Repo: ${response.body().toString()}")

                repoAdapter.submitList(response.body())
            }

            override fun onFailure(call: Call<List<Repo>>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }
}