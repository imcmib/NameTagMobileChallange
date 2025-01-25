package com.aivanchenko.nametag.data.service

import com.aivanchenko.nametag.data.model.ImageUploadResponseDto
import okhttp3.MultipartBody
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface MagicApiService {

    @Multipart
    @POST("api/v1/magicapi/image-upload/upload")
    suspend fun uploadImage(
        @Header("x-magicapi-key") apiKey: String = "cm6bgx3yi0001ky03dfhix3xw", // TODO: secure api key
        @Part part: MultipartBody.Part,
    ): ImageUploadResponseDto
}
