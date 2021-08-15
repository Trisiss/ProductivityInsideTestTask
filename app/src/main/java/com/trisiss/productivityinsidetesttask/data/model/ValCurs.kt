package com.trisiss.productivityinsidetesttask.data.model

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement

/**
 * Created by trisiss on 8/14/2021.
 */

class ValCurs {
    @JacksonXmlProperty(localName = "Data")
    var data: String = ""
    @JacksonXmlProperty
    var name: String = ""
    @JacksonXmlProperty(localName = "Valute")
    @JacksonXmlElementWrapper(useWrapping = false)
    var valutes: List<Valute> = ArrayList()
        set(value) {
            field = valutes + value
        }
}