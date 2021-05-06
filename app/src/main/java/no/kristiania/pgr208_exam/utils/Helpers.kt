package no.kristiania.pgr208_exam.utils
import java.text.DecimalFormat

fun String?.formatDecimal(): String {
    return if (this === null) {
        "null"
    } else {
        val priceUsd = this.toBigDecimal()
        val format = DecimalFormat("#,###.00")
        format.isParseBigDecimal = true
        format.minimumIntegerDigits = 1
        format.format(priceUsd)
    }
}

fun String?.formatDecimal(pattern : String?): String {
    return if (this === null) {
        "null"
    } else {
        var format = DecimalFormat("#,###.00")
        if (pattern !== null) {
            format = DecimalFormat(pattern)
        }
        val priceUsd = this.toBigDecimal()
        format.isParseBigDecimal = true
        format.minimumIntegerDigits = 1
        format.format(priceUsd)
    }
}