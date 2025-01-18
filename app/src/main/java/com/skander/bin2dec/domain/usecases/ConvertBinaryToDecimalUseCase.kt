package com.skander.bin2dec.domain.usecases

import com.skander.bin2dec.domain.model.BinaryNumber
import javax.inject.Inject

class ConvertBinaryToDecimalUseCase @Inject constructor(){
    operator fun invoke(input:String) : Result<Int> = runCatching {
        val binary = BinaryNumber(input)
        when {
            binary.isExceedMaxLength() -> throw IllegalArgumentException("Maximum 8 digits allowed")
            !binary.isValid() -> throw IllegalArgumentException("Invalid binary number")
            else -> binary.toDecimal() ?: throw IllegalArgumentException("Conversion failed")
        }
    }
}