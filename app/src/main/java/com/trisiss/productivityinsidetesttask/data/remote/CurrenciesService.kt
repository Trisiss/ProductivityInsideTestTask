package com.trisiss.productivityinsidetesttask.data.remote

import com.trisiss.productivityinsidetesttask.data.model.ValCurs
import retrofit2.Response

/**
 * Created by trisiss on 8/14/2021.
 */
interface CurrenciesService {
    suspend fun load(): Response<ValCurs>
}