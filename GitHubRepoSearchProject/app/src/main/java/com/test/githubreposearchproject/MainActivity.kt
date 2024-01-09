package com.test.githubreposearchproject

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.test.githubreposearchproject.adapter.UserAdapter
import com.test.githubreposearchproject.databinding.ActivityMainBinding
import com.test.githubreposearchproject.datamodel.UserDto
import com.test.githubreposearchproject.network.GithubService
import kotlinx.coroutines.Runnable
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    private lateinit var activityMainBinding: ActivityMainBinding
    private lateinit var userAdapter: UserAdapter
    private val handler = Handler(Looper.getMainLooper())
    private var searchText = StringBuffer()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)

        val runnable = Runnable{
            searchUser()
        }


        activityMainBinding.run {
            userAdapter = UserAdapter{
                val intent = Intent(this@MainActivity,RepoActivity::class.java)
                intent.putExtra("userName",it.userName)
                startActivity(intent)
            }

            //리사이클러뷰 설정
            userRecyclerView.run {
                layoutManager = LinearLayoutManager(this@MainActivity)
                adapter = userAdapter
            }

            //검색 EditText 설정
            searchEditText.addTextChangedListener {
                searchText.replace(0,searchText.capacity(),it.toString())
                handler.removeCallbacks(runnable)
                handler.postDelayed(
                    runnable,
                    300
                )
            }
        }
    }

    fun searchUser() {
        val githubService = APIClient.retrofit.create(GithubService::class.java)
        githubService.searchUsers(searchText).enqueue(object : Callback<UserDto> {
            override fun onResponse(call: Call<UserDto>, response: Response<UserDto>) {
                Log.d("testt", "Search User: ${response.body().toString()}")
                userAdapter.submitList(response.body()?.items)

            }

            override fun onFailure(call: Call<UserDto>, t: Throwable) {
                Toast.makeText(this@MainActivity,"통신 문제가 발생해 검색을 실패했습니다",Toast.LENGTH_SHORT).show()
                Log.e("UserNetWork","유저 검색 실패 $t")
            }
        })
    }
}
