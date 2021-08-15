package com.trisiss.productivityinsidetesttask

import android.text.InputFilter
import android.text.Spanned

/**
 * Created by trisiss on 8/14/2021.
 */
enum class SideElement{
    RIGHT, LEFT
}


/**
 * Фильтр для запрета количества вводимых знаков после запятой
 */
class DecimalDigitsInputFilter (private val decimalDigits: Int) : InputFilter {
    override fun filter(
        source: CharSequence,
        start: Int,
        end: Int,
        dest: Spanned,
        dstart: Int,
        dend: Int,
    ): CharSequence? {
        var dotPos = -1
        val len = dest.length
        for (i in 0 until len) {
            val c = dest[i]
            if (c == '.' || c == ',') {
                dotPos = i
                break
            }
        }
        if (dotPos >= 0) {

            // protects against many dots
            if (source == "." || source == ",") {
                return ""
            }
            // if the text is entered before the dot
            if (dend <= dotPos) {
                return null
            }
            if (len - dotPos > decimalDigits) {
                return ""
            }
        }
        return null
    }
}