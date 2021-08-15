package com.trisiss.productivityinsidetesttask.data.model

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty

/**
 * Created by trisiss on 8/14/2021.
 */
data class Valute(
    @JacksonXmlProperty(localName = "ID")
    val id: String,
    @JacksonXmlProperty(localName = "NumCode")
    val code: Int,
    @JacksonXmlProperty(localName = "CharCode")
    val charCode: String,
    @JacksonXmlProperty(localName = "Nominal")
    val nominal: Int,
    @JacksonXmlProperty(localName = "Name")
    val name: String,
    @JacksonXmlProperty(localName = "Value")
    val value: String,
)
