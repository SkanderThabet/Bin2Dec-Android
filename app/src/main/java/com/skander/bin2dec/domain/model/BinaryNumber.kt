package com.skander.bin2dec.domain.model

data class BinaryNumber(val value: String) {
    fun isValid(): Boolean =
        value.all { it == '0' || it == '1' }

    fun isExceedMaxLength() : Boolean = value.length > MAX_LENGTH

    fun toDecimal(): Int? =
        if (isValid() && value.isNotEmpty()) {
            value.reversed()
                .mapIndexed { index, digit ->
                    digit.toString().toInt() * Math.pow(2.0, index.toDouble())
                }
                .sum()
                .toInt()
        } else null

    companion object {
        const val MAX_LENGTH = 8
    }
}