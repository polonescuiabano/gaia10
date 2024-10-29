package com.example.gaia

import okhttp3.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaType
import java.io.IOException


class PostgresHelper {
    private val client = OkHttpClient()
    private val baseUrl = "http://10.0.2.2:5000/"
    private val gson = Gson()

    fun storeDiagnosis(username: String, diagnosis: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val diagnosisRequest = DiagnosisRequest(username, diagnosis)
            val json = gson.toJson(diagnosisRequest)

            val requestBody = RequestBody.create(
                "application/json; charset=utf-8".toMediaType(),
                json
            )

            val request = Request.Builder()
                .url("${baseUrl}diagnosis")
                .post(requestBody)
                .build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    println("Erro ao conectar à API: ${e.message}")
                }

                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful) {
                        println("Diagnóstico armazenado com sucesso!")
                    } else {
                        val responseBody = response.body
                        println("Erro ao armazenar diagnóstico: ${responseBody?.string()}")
                    }
                }
            })
        }
    }

    data class DiagnosisRequest(val username: String, val diagnosis: String)
}
