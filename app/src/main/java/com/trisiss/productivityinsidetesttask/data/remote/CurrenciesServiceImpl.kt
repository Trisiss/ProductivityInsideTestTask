package com.trisiss.productivityinsidetesttask.data.remote

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.trisiss.productivityinsidetesttask.data.model.ValCurs
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory


/**
 * Created by trisiss on 8/14/2021.
 */
class CurrenciesServiceImpl: CurrenciesService {
    private val SERVER = "http://www.cbr.ru/"
    var retrofit: Retrofit
    var currencyApi: CurrencyApi

    init {
        retrofit = initRetrofit()
        currencyApi = retrofit.create(CurrencyApi::class.java)
    }

    private fun initRetrofit(): Retrofit {
        val interceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        val client = OkHttpClient.Builder().apply {
            networkInterceptors().add(Interceptor { chain ->
                val original = chain.request()
                val request = original.newBuilder()
                    .method(original.method, original.body)
                    .build()
                chain.proceed(request)
            })
            addInterceptor(interceptor)
        }
        val mapper = ObjectMapper().registerKotlinModule()
        return Retrofit.Builder().baseUrl(SERVER)
            .addConverterFactory(JacksonConverterFactory.create(mapper))
            .client(client.build())
            .build()

    }

    override suspend fun load(): Response<ValCurs> = currencyApi.getValutes()
}