package com.example.gaia

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

data class GaiaRequest(val message: String, val username: String)
data class GaiaResponse(val response: String)

interface GaiaApiService {
    @POST("/gaia")
    fun chatWithGaia(@Body request: GaiaRequest): Call<GaiaResponse>
}
