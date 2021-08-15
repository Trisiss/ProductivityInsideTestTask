package com.trisiss.productivityinsidetesttask.data.remote

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
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
    private val SERVER = "https://www.cbr.ru/"
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

        val kotlinModule: KotlinModule = KotlinModule.Builder()
            .strictNullChecks(false)
            .nullIsSameAsDefault(true) // needed, else it will break for null https://github.com/FasterXML/jackson-module-kotlin/issues/130#issuecomment-546625625
            .build()

        val xmlMapper =
            XmlMapper(JacksonXmlModule())
                .registerModule(kotlinModule)
                .enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
                .enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)

        return Retrofit.Builder().baseUrl(SERVER)
            .addConverterFactory(JacksonConverterFactory.create(xmlMapper))
            .client(client.build())
            .build()

    }

    override suspend fun load(): Response<ValCurs> = currencyApi.getValutes()
}