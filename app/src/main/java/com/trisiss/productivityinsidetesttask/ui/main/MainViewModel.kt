package com.trisiss.productivityinsidetesttask.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.trisiss.productivityinsidetesttask.SideElement
import com.trisiss.productivityinsidetesttask.data.model.ValCurs
import com.trisiss.productivityinsidetesttask.data.model.Valute
import com.trisiss.productivityinsidetesttask.data.remote.CurrenciesService
import kotlinx.coroutines.*
import retrofit2.Response

class MainViewModel(val currencyService: CurrenciesService) : ViewModel() {
    private var _listCurrencies: MutableLiveData<List<Valute>> = MutableLiveData()
    val listCurrencies: LiveData<List<Valute>>
        get() = _listCurrencies

    private var _leftCurrency: MutableLiveData<Valute> = MutableLiveData()
    val leftCurrency: LiveData<Valute>
        get() = _leftCurrency


    private var _rightCurrency: MutableLiveData<Valute> = MutableLiveData()
    val rightCurrency: LiveData<Valute>
        get() = _rightCurrency

    var response: Response<ValCurs>? = null
    var error: MutableLiveData<String>? = null
    val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        error?.value = throwable.localizedMessage
    }

    init {
        loadCurrency()
    }

    fun loadCurrency() {
        val ruCurrency = Valute(id = "0",
            code = 0,
            charCode = "RU",
            nominal = 1,
            name = "Российский рубль",
            value = "1")
        CoroutineScope(Dispatchers.IO + exceptionHandler).launch(exceptionHandler) {
            response = currencyService.load()
            withContext(Dispatchers.Main + exceptionHandler) {
                if (response!!.isSuccessful) {
                    val valutes = ArrayList(response!!.body()?.valutes)
                    valutes.add(0, ruCurrency)
                    _listCurrencies.postValue(valutes)
                } else {
                    error?.postValue(response?.message().toString())
                }
            }
            withContext(Dispatchers.Main) {
                _leftCurrency.postValue(_listCurrencies.value?.firstOrNull())
                _rightCurrency.postValue(_listCurrencies.value?.firstOrNull())
            }
        }
    }

    fun changeCurrency(side: SideElement, index: Int) {
        val newValue = listCurrencies.value?.get(index)
        when (side) {
            SideElement.RIGHT -> _rightCurrency.value = newValue
            SideElement.LEFT -> _leftCurrency.value = newValue
        }
    }
}