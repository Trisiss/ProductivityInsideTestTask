package com.trisiss.productivityinsidetesttask.di

import com.trisiss.productivityinsidetesttask.data.remote.CurrenciesService
import com.trisiss.productivityinsidetesttask.data.remote.CurrenciesServiceImpl
import com.trisiss.productivityinsidetesttask.ui.main.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Created by trisiss on 8/14/2021.
 */
val appModule = module{
    // ViewModels
    viewModel { MainViewModel(get()) }
    // Services
    single<CurrenciesService> { CurrenciesServiceImpl() }
}