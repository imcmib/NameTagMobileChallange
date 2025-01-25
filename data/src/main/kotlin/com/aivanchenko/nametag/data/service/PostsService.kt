package com.aivanchenko.nametag.data.service

import com.aivanchenko.nametag.data.model.PostBodyDto
import com.aivanchenko.nametag.data.model.PostsDto
import com.aivanchenko.nametag.data.model.VoteDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface PostsService {

    @GET("posts")
    suspend fun getPosts(): PostsDto

    @POST("posts/{postId}/upvote")
    suspend fun upVote(
        @Path("postId") postId: String,
    ): VoteDto

    @POST("posts/{postId}/downvote")
    suspend fun downVote(
        @Path("postId") postId: String,
    ): VoteDto

    @POST("posts")
    suspend fun createPost(
        @Body body: PostBodyDto,
    )
}
