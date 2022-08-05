package com.danilp.aquariumhelper.domain.use_case.validation

import android.content.Context
import com.danilp.aquariumhelper.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ValidateTemperature @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun execute(temperature: String): ValidationResult =
        if (temperature.isBlank())
            ValidationResult(
                successful = false,
                errorMessage = context.getString(R.string.this_field_cant_be_blank_validation_res)
            )
        else if (temperature.toDoubleOrNull() == null)
            ValidationResult(
                successful = false,
                errorMessage = context.getString(R.string.should_be_decimal_validation_res)
            )
        else
            ValidationResult(successful = true)
}