package com.test.githubreposearchproject.network

import com.test.githubreposearchproject.datamodel.Repo
import com.test.githubreposearchproject.datamodel.UserDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface GithubService {
    @Headers("Authorization: Bearer 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")
    @GET("users/{username}/repos")
    fun listRepos(@Path("username") username:String): Call<List<Repo>>


    @Headers("Authorization: Bearer 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")
    @GET("search/users")
    fun searchUsers(@Query("q")query:StringBuffer):Call<UserDto>
}