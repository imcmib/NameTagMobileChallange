package com.aivanchenko.nametag.domain.repository

import com.aivanchenko.nametag.domain.model.Post

interface PostsRepository {

    /**
     * Fetches the list of posts.
     */
    suspend fun getPosts(): List<Post>

    /**
     * Upvotes the post.
     *
     * @param postId The ID of the post to upvote.
     * @return The number of upvotes for the post.
     */
    suspend fun upVote(postId: String): Long

    /**
     * Downvotes the post.
     *
     * @param postId The ID of the post to downvote.
     * @return The number of upvotes for the post.
     */
    suspend fun downVote(postId: String): Long

    /**
     * Creates a new post.
     *
     * @param imagePath The Uri of the image for the post.
     * @param headline The headline for the post.
     * @return The newly created post.
     */
    suspend fun createPost(
        imagePath: String,
        headline: String,
    )
}
