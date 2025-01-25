package com.aivanchenko.nametag.data.repository

import android.content.Context
import android.net.Uri
import androidx.core.net.toUri
import com.aivanchenko.nametag.data.model.PostBodyDto
import com.aivanchenko.nametag.data.model.toPost
import com.aivanchenko.nametag.data.provider.DispatchersProvider
import com.aivanchenko.nametag.data.service.MagicApiService
import com.aivanchenko.nametag.data.service.PostsService
import com.aivanchenko.nametag.domain.model.Post
import com.aivanchenko.nametag.domain.repository.PostsRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import javax.inject.Inject
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody

class PostsRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val dispatchersProvider: DispatchersProvider,
    private val postsService: PostsService,
    private val magicApiService: MagicApiService,
) : PostsRepository {

    override suspend fun getPosts(): List<Post> =
        withContext(dispatchersProvider.io) {
            postsService.getPosts().posts.map { it.toPost() }
        }

    override suspend fun upVote(postId: String) = withContext(dispatchersProvider.io) {
        postsService.upVote(postId = postId).upvotes
    }

    override suspend fun downVote(postId: String) = withContext(dispatchersProvider.io) {
        postsService.downVote(postId = postId).upvotes
    }

    override suspend fun createPost(
        imagePath: String,
        headline: String,
    ) = withContext(dispatchersProvider.io) {
        val part = prepareFilePartFromUri(imagePath.toUri())
        val url = magicApiService.uploadImage(part = part).url

        postsService.createPost(
            body = PostBodyDto(
                headline = headline,
                image = url
            )
        )
    }

    fun prepareFilePartFromUri(uri: Uri): MultipartBody.Part {
        val contentResolver = context.contentResolver
        val mimeType = contentResolver.getType(uri) ?: "application/octet-stream"

        val inputStream: InputStream? = contentResolver.openInputStream(uri)
        val tempFile = File(context.cacheDir, "temp_file")
        inputStream.use { input ->
            FileOutputStream(tempFile).use { output ->
                input?.copyTo(output)
            }
        }

        val requestFile = tempFile.asRequestBody(mimeType.toMediaTypeOrNull())
        return MultipartBody.Part.createFormData("filename", tempFile.name, requestFile)
    }
}
