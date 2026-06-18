package com.fitpulse.app.network

import retrofit2.http.GET

interface WgerApiService {

    @GET("exercisecategory/?format=json")
    suspend fun getCategories(): WgerListResponse<WgerCategory>

    @GET("muscle/?format=json")
    suspend fun getMuscles(): WgerListResponse<WgerMuscle>
}
