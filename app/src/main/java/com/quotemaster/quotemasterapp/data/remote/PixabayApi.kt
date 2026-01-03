package com.quotemaster.quotemasterapp.data.remote

import com.quotemaster.quotemasterapp.data.model.PixabayResponse
import com.quotemaster.quotemasterapp.utils.Constants.PIXABAY_KEY
import retrofit2.http.GET
import retrofit2.http.Query

interface PixabayApi {
    @GET("/api/")
    suspend fun searchImages(
        @Query("key") apiKey: String = PIXABAY_KEY,
        @Query("q") query: String,
        @Query("image_type") imageType: String = "photo",
        @Query("per_page") perPage: Int
    ): PixabayResponse
}


