package com.trisiss.productivityinsidetesttask.data.remote

import com.trisiss.productivityinsidetesttask.data.model.ValCurs
import retrofit2.Response
import retrofit2.http.GET

/**
 * Created by trisiss on 8/14/2021.
 */
interface CurrencyApi {

    @GET(value = "scripts/XML_daily.asp")
    suspend fun getValutes(): Response<ValCurs>
}