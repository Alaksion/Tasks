package com.example.tasks.service.repository.remote

import com.example.tasks.service.constants.TaskConstants
import com.example.tasks.service.repository.local.SecurityPreferences
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient private constructor(){


    companion object {
        private lateinit var retrofit : Retrofit
        private var tokenKey = ""
        private var personKey = ""

        private fun getInstance() : Retrofit{
            val httpClient = OkHttpClient.Builder()
            val baseUrl = "http://devmasterteam.com/CursoAndroidAPI/"

            // interceptador de requests que adiciona aos headers as requests
            httpClient.addInterceptor(object : Interceptor{
                override fun intercept(chain: Interceptor.Chain): Response {
                    val request = chain.request()
                        .newBuilder()
                        .addHeader(TaskConstants.HEADER.TOKEN_KEY, tokenKey)
                        .addHeader(TaskConstants.HEADER.PERSON_KEY, personKey)
                        .build()
                    return chain.proceed(request)
                }
            })

            if(!Companion::retrofit.isInitialized){
                synchronized(RetrofitClient::class.java){
                    retrofit = Retrofit.Builder()
                        .baseUrl(baseUrl)
                        .client(httpClient.build())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
                }
            }
            return retrofit
        }

        fun <T>createService(serviceClass : Class<T>) : T {
            return getInstance()
                .create(serviceClass)
        }

         fun setHeaders(token: String, personKey: String){
            this.personKey = personKey
            this.tokenKey = token
        }
    }
}