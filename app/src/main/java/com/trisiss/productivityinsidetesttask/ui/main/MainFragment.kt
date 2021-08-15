package com.trisiss.productivityinsidetesttask.ui.main

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.trisiss.productivityinsidetesttask.DecimalDigitsInputFilter
import com.trisiss.productivityinsidetesttask.SideElement
import com.trisiss.productivityinsidetesttask.databinding.MainFragmentBinding
import com.trisiss.productivityinsidetesttask.ui.SelectDialog
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.lang.NumberFormatException
import java.math.RoundingMode
import java.text.NumberFormat
import java.util.*

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    val viewModel: MainViewModel by viewModel()
    private lateinit var binding: MainFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = MainFragmentBinding.inflate(layoutInflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.vm = viewModel
        binding.changeCurrencyLeft.setOnClickListener {
            changeCurrency(side = SideElement.LEFT)
        }
        binding.changeCurrencyRight.setOnClickListener {
            changeCurrency(side = SideElement.RIGHT)
        }
        binding.currencyLeft.addTextChangedListener(object : TextWatcher {
            var old: String = ""
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                old = s.toString()
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Nothing
            }

            override fun afterTextChanged(s: Editable?) {
                // Пересчет только, если значение изменилось
                if (old != s.toString()) reCalculate(side = SideElement.LEFT)
            }
        })
        binding.currencyRight.addTextChangedListener(object : TextWatcher {
            var old: String = ""
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                old = s.toString()
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Nothing
            }

            override fun afterTextChanged(s: Editable?) {
                // Пересчет только, если значени изменилось
                if (old != s.toString()) reCalculate(side = SideElement.RIGHT)
            }
        })
        binding.currencyLeft.filters = arrayOf(DecimalDigitsInputFilter(decimalDigits = 2))
        viewModel.listCurrencies.observe(viewLifecycleOwner, {
            binding.apply {
                stateLoading(on = false)
            }
        })
        viewModel.rightCurrency.observe(viewLifecycleOwner, {
            reCalculate(SideElement.RIGHT)
        })
        viewModel.leftCurrency.observe(viewLifecycleOwner, {
            reCalculate(SideElement.LEFT)
        })

        stateLoading(on = true)
        return binding.root
    }

    fun changeCurrency(side: SideElement) {
        val listener = object : OnDialogItemSelected {
            override fun onItemSelected(index: Int) {
                viewModel.changeCurrency(side = side, index = index)
            }
        }
        val listCurrenciesStringList =
            viewModel.listCurrencies.value?.map { item -> "${item.name} (${item.charCode})" }
        val listCurrenciesString = listCurrenciesStringList?.toTypedArray()
        val dialog = SelectDialog(list = listCurrenciesString!!, listener = listener)
        dialog.show(parentFragmentManager, tag)
    }

    fun reCalculate(side: SideElement) {
        // Пересчет значения в editText
        try {
            var leftValue = 0f
            var rightValue = 0f
            val leftValueString = binding.currencyLeft.text.toString()
            val rightValueString = binding.currencyRight.text.toString()

            // Проверки на пустые editText
            if (leftValueString.isNotEmpty()) leftValue = leftValueString.toFloat()
            if (rightValueString.isNotEmpty()) rightValue = rightValueString.toFloat()

            // Заменяем запятую на точку для парсинка во float
            var leftCurrency = viewModel.leftCurrency.value?.value?.replace(',', '.')?.toFloat()
            var rightCurrency = viewModel.rightCurrency.value?.value?.replace(',', '.')?.toFloat()

            if ((leftCurrency != null) && (rightCurrency != null)) {
                when (side) {
                    SideElement.LEFT -> {
                        // Не изменяем editText, если он в фокусе, чтобы не менялась позиция указателя
                        if (binding.currencyRight.isFocused) return
                        rightValue = (leftValue * leftCurrency) / rightCurrency
                        // Округляем до двух знаков после запятой
                        rightValue = rightValue.toBigDecimal().setScale(2, RoundingMode.CEILING).toFloat()
                        binding.currencyRight.setText(rightValue.toString())
                    }
                    SideElement.RIGHT -> {
                        // Не изменяем editText, если он в фокусе, чтобы не менялась позиция указателя
                        if (binding.currencyLeft.isFocused) return
                        leftValue = (rightValue * rightCurrency) / leftCurrency
                        // Округляем до двух знаков после запятой
                        leftValue = leftValue.toBigDecimal().setScale(2, RoundingMode.CEILING).toFloat()
                        binding.currencyLeft.setText(leftValue.toString())
                    }
                }
            }
        } catch (e: Exception) {
            Log.e(tag, "reCalculate: ${e.localizedMessage}")
        }
    }

    fun stateLoading(on: Boolean) {
        // Изменяем состояние загрузки
        var visible: Int
        if (on) {
            visible = View.GONE
            binding.progressCircular.visibility = View.VISIBLE
        } else {
            visible = View.VISIBLE
            binding.progressCircular.visibility = View.GONE
        }
        binding.apply {
            changeCurrencyLeft.visibility = visible
            changeCurrencyRight.visibility = visible
            textCurrencyLeft.visibility = visible
            textCurrencyRight.visibility = visible
            currencyLeft.visibility = visible
            currencyRight.visibility = visible
        }
    }

}

interface OnDialogItemSelected {
    fun onItemSelected(index: Int)
}