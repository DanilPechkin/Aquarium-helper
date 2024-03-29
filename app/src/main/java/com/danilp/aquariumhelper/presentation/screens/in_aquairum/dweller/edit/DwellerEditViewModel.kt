package com.danilp.aquariumhelper.presentation.screens.in_aquairum.dweller.edit

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.danilp.aquariumhelper.R
import com.danilp.aquariumhelper.domain.dweller.model.Dweller
import com.danilp.aquariumhelper.domain.dweller.repository.DwellerRepository
import com.danilp.aquariumhelper.domain.service.LogService
import com.danilp.aquariumhelper.domain.use_case.calculation.conversion.alkalinity.ConvertDKH
import com.danilp.aquariumhelper.domain.use_case.calculation.conversion.capacity.ConvertLiters
import com.danilp.aquariumhelper.domain.use_case.calculation.conversion.temperature.ConvertCelsius
import com.danilp.aquariumhelper.domain.use_case.validation.Validate
import com.danilp.aquariumhelper.presentation.screens.ProfessionalAquaristViewModel
import com.danilp.aquariumhelper.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DwellerEditViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val repository: DwellerRepository,
    private val savedStateHandle: SavedStateHandle,
    private val validate: Validate,
    private val convertCelsius: ConvertCelsius,
    private val convertLiters: ConvertLiters,
    private val convertDKH: ConvertDKH,
    logService: LogService
) : ProfessionalAquaristViewModel(logService) {

    var state by mutableStateOf(DwellerEditState())

    private val validationEventChannel = Channel<ValidationEvent>()
    val validationEvents = validationEventChannel.receiveAsFlow()

    private lateinit var measureCubicFeet: String
    private lateinit var measureUSCups: String
    private lateinit var measureTeaspoons: String
    private lateinit var measureTablespoons: String
    private lateinit var measureMilliliters: String
    private lateinit var measureMetricCups: String
    private lateinit var measureGallons: String
    private lateinit var measureCubicMeters: String
    private lateinit var measureCubicInches: String
    private lateinit var measureLiters: String
    private lateinit var measureCelsius: String
    private lateinit var measureKelvin: String
    private lateinit var measureFahrenheit: String
    private lateinit var measureDKH: String
    private lateinit var measureMeqL: String
    private lateinit var measurePpm: String

    init {
        viewModelScope.launch(logErrorExceptionHandler) {
            val id = savedStateHandle.get<Int>("dwellerId") ?: return@launch
            state = state.copy(isLoading = true)
            val dwellerInfoResult = repository.findDwellerById(id)

            dwellerInfoResult.collect { result ->
                state = when (result) {
                    is Resource.Success -> {
                        state.copy(
                            dweller = result.data ?: Dweller.createEmpty(),
                            isLoading = false,
                            error = null
                        )
                    }
                    is Resource.Loading -> {
                        state.copy(isLoading = result.isLoading)
                    }
                    is Resource.Error -> {
                        state.copy(isLoading = false, error = result.message)
                    }
                }
            }

            val sharedPreferences = context.getSharedPreferences(
                context.getString(R.string.in_aquarium_info_shared_preferences_key),
                Context.MODE_PRIVATE
            )

            state = state.copy(
                dweller = state.dweller.copy(
                    aquariumId = sharedPreferences.getInt(
                        context.getString(R.string.saved_aquarium_id_key),
                        0
                    )
                ),
                tempMeasure = sharedPreferences.getString(
                    context.getString(R.string.temperature_measure_id_key),
                    context.getString(R.string.temp_measure_celsius)
                ) ?: context.getString(R.string.temp_measure_celsius),
                alkalinityMeasure = sharedPreferences.getString(
                    context.getString(R.string.alkalinity_measure_id_key),
                    context.getString(R.string.alkalinity_measure_dkh)
                ) ?: context.getString(R.string.alkalinity_measure_dkh),
                capacityMeasure = sharedPreferences.getString(
                    context.getString(R.string.capacity_measure_id_key),
                    context.getString(R.string.capacity_measure_liters)
                ) ?: context.getString(R.string.capacity_measure_liters)
            )

            measureCubicFeet = context.getString(R.string.capacity_measure_cubic_feet)
            measureUSCups = context.getString(R.string.capacity_measure_us_cups)
            measureTeaspoons = context.getString(R.string.capacity_measure_teaspoons)
            measureTablespoons = context.getString(R.string.capacity_measure_tablespoons)
            measureMilliliters = context.getString(R.string.capacity_measure_milliliters)
            measureMetricCups = context.getString(R.string.capacity_measure_metric_cups)
            measureGallons = context.getString(R.string.capacity_measure_gallons)
            measureCubicMeters = context.getString(R.string.capacity_measure_cubic_meters)
            measureCubicInches = context.getString(R.string.capacity_measure_cubic_inches)
            measureLiters = context.getString(R.string.capacity_measure_liters)
            measureCelsius = context.getString(R.string.temp_measure_celsius)
            measureFahrenheit = context.getString(R.string.temp_measure_fahrenheit)
            measureKelvin = context.getString(R.string.temp_measure_kelvin)
            measureDKH = context.getString(R.string.alkalinity_measure_dkh)
            measurePpm = context.getString(R.string.alkalinity_measure_ppm)
            measureMeqL = context.getString(R.string.alkalinity_measure_meql)

            state = state.copy(
                name = state.dweller.name,
                genus = state.dweller.genus,
                amount = if (state.dweller.amount == 0) "" else state.dweller.amount.toString(),
                minTemperature = if (state.dweller.minTemperature == 0.0) "" else
                    when (state.tempMeasure) {
                        measureCelsius -> state.dweller.minTemperature.toString()
                        measureKelvin -> convertCelsius.toKelvin(
                            celsius = state.dweller.minTemperature
                        ).result.toString()
                        measureFahrenheit -> convertCelsius.toFahrenheit(
                            celsius = state.dweller.minTemperature
                        ).result.toString()
                        else -> state.dweller.minTemperature.toString()
                    },
                maxTemperature = if (state.dweller.maxTemperature == 0.0) "" else
                    when (state.tempMeasure) {
                        measureCelsius -> state.dweller.maxTemperature.toString()
                        measureKelvin -> convertCelsius.toKelvin(
                            celsius = state.dweller.maxTemperature
                        ).result.toString()
                        measureFahrenheit -> convertCelsius.toFahrenheit(
                            celsius = state.dweller.maxTemperature
                        ).result.toString()
                        else -> state.dweller.maxTemperature.toString()
                    },
                liters = if (state.dweller.liters == 0.0) "" else
                    when (state.capacityMeasure) {
                        measureLiters -> state.dweller.liters.toString()
                        measureCubicFeet -> convertLiters.toCubicFeet(
                            liters = state.dweller.liters
                        ).result.toString()
                        measureUSCups -> convertLiters.toUSCups(
                            liters = state.dweller.liters
                        ).result.toString()
                        measureTeaspoons -> convertLiters.toTeaspoons(
                            liters = state.dweller.liters
                        ).result.toString()
                        measureTablespoons -> convertLiters.toTablespoons(
                            liters = state.dweller.liters
                        ).result.toString()
                        measureMilliliters -> convertLiters.toMilliliters(
                            liters = state.dweller.liters
                        ).result.toString()
                        measureMetricCups -> convertLiters.toMetricCups(
                            liters = state.dweller.liters
                        ).result.toString()
                        measureGallons -> convertLiters.toGallons(
                            liters = state.dweller.liters
                        ).result.toString()
                        measureCubicMeters -> convertLiters.toCubicMeters(
                            liters = state.dweller.liters
                        ).result.toString()
                        measureCubicInches -> convertLiters.toCubicInches(
                            liters = state.dweller.liters
                        ).result.toString()
                        else -> state.dweller.liters.toString()
                    },
                minPh = if (state.dweller.minPh == 0.0) "" else
                    when (state.alkalinityMeasure) {
                        measureDKH -> state.dweller.minPh.toString()
                        measurePpm -> convertDKH.toPpm(dKH = state.dweller.minPh).result.toString()
                        measureMeqL -> convertDKH.toMeqL(dKH = state.dweller.minPh).result.toString()
                        else -> state.dweller.minPh.toString()
                    },
                maxPh = if (state.dweller.maxPh == 0.0) "" else
                    when (state.alkalinityMeasure) {
                        measureDKH -> state.dweller.maxPh.toString()
                        measurePpm -> convertDKH.toPpm(dKH = state.dweller.maxPh).result.toString()
                        measureMeqL -> convertDKH.toMeqL(dKH = state.dweller.maxPh).result.toString()
                        else -> state.dweller.maxPh.toString()
                    },
                minGh = if (state.dweller.minGh == 0.0) "" else
                    when (state.alkalinityMeasure) {
                        measureDKH -> state.dweller.minGh.toString()
                        measurePpm -> convertDKH.toPpm(dKH = state.dweller.minGh).result.toString()
                        measureMeqL -> convertDKH.toMeqL(dKH = state.dweller.minGh).result.toString()
                        else -> state.dweller.minGh.toString()
                    },
                maxGh = if (state.dweller.maxGh == 0.0) "" else
                    when (state.alkalinityMeasure) {
                        measureDKH -> state.dweller.maxGh.toString()
                        measurePpm -> convertDKH.toPpm(dKH = state.dweller.maxGh).result.toString()
                        measureMeqL -> convertDKH.toMeqL(dKH = state.dweller.maxGh).result.toString()
                        else -> state.dweller.maxGh.toString()
                    },
                minKh = if (state.dweller.minKh == 0.0) "" else
                    when (state.alkalinityMeasure) {
                        measureDKH -> state.dweller.minKh.toString()
                        measurePpm -> convertDKH.toPpm(dKH = state.dweller.minKh).result.toString()
                        measureMeqL -> convertDKH.toMeqL(dKH = state.dweller.minKh).result.toString()
                        else -> state.dweller.minKh.toString()
                    },
                maxKh = if (state.dweller.maxKh == 0.0) "" else
                    when (state.alkalinityMeasure) {
                        measureDKH -> state.dweller.maxKh.toString()
                        measurePpm -> convertDKH.toPpm(dKH = state.dweller.maxKh).result.toString()
                        measureMeqL -> convertDKH.toMeqL(dKH = state.dweller.maxKh).result.toString()
                        else -> state.dweller.maxKh.toString()
                    },
                description = state.dweller.description
            )
        }
    }

    fun onEvent(event: DwellerEditEvent) {
        when (event) {
            is DwellerEditEvent.InsertButtonPressed -> {
                submitData()
            }
            is DwellerEditEvent.DeleteButtonPressed -> {
                viewModelScope.launch(logErrorExceptionHandler) {
                    delete(state.dweller)
                    validationEventChannel.send(ValidationEvent.Success)
                }
            }
            is DwellerEditEvent.NameChanged -> {
                state = state.copy(name = event.name)
            }
            is DwellerEditEvent.GenusChanged -> {
                state = state.copy(genus = event.genus)
            }
            is DwellerEditEvent.AmountChanged -> {
                state = state.copy(amount = event.amount)
            }
            is DwellerEditEvent.MinTemperatureChanged -> {
                state = state.copy(minTemperature = event.temp)
            }
            is DwellerEditEvent.MaxTemperatureChanged -> {
                state = state.copy(maxTemperature = event.temp)
            }
            is DwellerEditEvent.LitersChanged -> {
                state = state.copy(liters = event.liters)
            }
            is DwellerEditEvent.MinPhChanged -> {
                state = state.copy(minPh = event.ph)
            }
            is DwellerEditEvent.MaxPhChanged -> {
                state = state.copy(maxPh = event.ph)
            }
            is DwellerEditEvent.MinGhChanged -> {
                state = state.copy(minGh = event.gh)
            }
            is DwellerEditEvent.MaxGhChanged -> {
                state = state.copy(maxGh = event.gh)
            }
            is DwellerEditEvent.MinKhChanged -> {
                state = state.copy(minKh = event.kh)
            }
            is DwellerEditEvent.MaxKhChanged -> {
                state = state.copy(maxKh = event.kh)
            }
            is DwellerEditEvent.DescriptionChanged -> {
                state = state.copy(description = event.description)
            }
            is DwellerEditEvent.ImagePicked -> {
                state = state.copy(
                    dweller = state.dweller.copy(imageUri = event.imageUri)
                )
            }
        }
    }

    private fun insert(dweller: Dweller) = viewModelScope.launch(logErrorExceptionHandler) {
        repository.insert(dweller)
    }

    private fun delete(dweller: Dweller) = viewModelScope.launch(logErrorExceptionHandler) {
        repository.delete(dweller)
    }

    private fun submitData() {
        val nameResult = validate.string(state.name)
        val amountResult = validate.integer(state.amount, isRequired = true)
        val minTemperatureResult = validate.decimal(state.minTemperature, isRequired = true)
        val maxTemperatureResult = validate.decimal(state.maxTemperature, isRequired = true)
        val litersResult = validate.decimal(state.liters, isRequired = true)
        val minPhResult = validate.decimal(state.minPh)
        val maxPhResult = validate.decimal(state.maxPh)
        val minGhResult = validate.decimal(state.minGh)
        val maxGhResult = validate.decimal(state.maxGh)
        val minKhResult = validate.decimal(state.minKh)
        val maxKhResult = validate.decimal(state.maxKh)

        val hasError = listOf(
            nameResult,
            amountResult,
            minTemperatureResult,
            maxTemperatureResult,
            litersResult,
            minPhResult,
            maxPhResult,
            minGhResult,
            maxGhResult,
            minKhResult,
            maxKhResult
        ).any { it.error != null }

        if (hasError) {
            state = state.copy(
                nameErrorCode = nameResult.error,
                amountError = amountResult.error,
                minTemperatureError = minTemperatureResult.error,
                maxTemperatureError = maxTemperatureResult.error,
                litersError = litersResult.error,
                minPhError = minPhResult.error,
                maxPhError = maxPhResult.error,
                minGhError = minGhResult.error,
                maxGhError = maxGhResult.error,
                minKhError = minKhResult.error,
                maxKhError = maxKhResult.error
            )
            return
        }

        viewModelScope.launch(logErrorExceptionHandler) {
            val isTempCorrect = (state.minTemperature.toDouble() < state.maxTemperature.toDouble())
            val isPhCorrect = (((state.minPh.toDoubleOrNull()
                ?: 0.0) < (state.maxPh.toDoubleOrNull() ?: 0.0)))
            val isGhCorrect = (((state.minGh.toDoubleOrNull()
                ?: 0.0) < (state.maxGh.toDoubleOrNull() ?: 0.0)))
            val isKhCorrect = (((state.minKh.toDoubleOrNull()
                ?: 0.0) < (state.maxKh.toDoubleOrNull() ?: 0.0)))

            if (!isTempCorrect) {
                kotlin.run {
                    val temp = state.minTemperature
                    state = state.copy(minTemperature = state.maxTemperature)
                    state = state.copy(maxTemperature = temp)
                }
            }

            if (!isPhCorrect) {
                kotlin.run {
                    val temp = state.minPh
                    state = state.copy(minPh = state.maxPh)
                    state = state.copy(maxPh = temp)
                }
            }

            if (!isGhCorrect) {
                kotlin.run {
                    val temp = state.minGh
                    state = state.copy(minGh = state.maxGh)
                    state = state.copy(maxGh = temp)
                }
            }

            if (!isKhCorrect) {
                kotlin.run {
                    val temp = state.minKh
                    state = state.copy(minKh = state.maxKh)
                    state = state.copy(maxKh = temp)
                }
            }

            state = state.copy(
                dweller = state.dweller.copy(
                    name = state.name,
                    genus = state.genus,
                    amount = state.amount.toInt(),
                    minTemperature =
                    when (state.tempMeasure) {
                        measureCelsius -> state.minTemperature.toDoubleOrNull() ?: 0.0
                        measureFahrenheit -> convertCelsius.toFahrenheit(
                            fahrenheit = state.minTemperature.toDoubleOrNull() ?: 0.0
                        ).result
                        measureKelvin -> convertCelsius.toKelvin(
                            kelvin = state.minTemperature.toDoubleOrNull() ?: 0.0
                        ).result
                        else -> state.minTemperature.toDoubleOrNull() ?: 0.0
                    },
                    maxTemperature =
                    when (state.tempMeasure) {
                        measureCelsius -> state.maxTemperature.toDoubleOrNull() ?: 0.0
                        measureFahrenheit -> convertCelsius.toFahrenheit(
                            fahrenheit = state.maxTemperature.toDoubleOrNull() ?: 0.0
                        ).result
                        measureKelvin -> convertCelsius.toKelvin(
                            kelvin = state.maxTemperature.toDoubleOrNull() ?: 0.0
                        ).result
                        else -> state.maxTemperature.toDoubleOrNull() ?: 0.0
                    },
                    liters =
                    when (state.capacityMeasure) {
                        measureLiters -> state.liters.toDoubleOrNull() ?: 0.0
                        measureCubicFeet -> convertLiters.toCubicFeet(
                            feet = state.liters.toDoubleOrNull() ?: 0.0
                        ).result
                        measureUSCups -> convertLiters.toUSCups(
                            cups = state.liters.toDoubleOrNull() ?: 0.0
                        ).result
                        measureTeaspoons -> convertLiters.toTeaspoons(
                            teaspoons = state.liters.toDoubleOrNull() ?: 0.0
                        ).result
                        measureTablespoons -> convertLiters.toTablespoons(
                            tablespoons = state.liters.toDoubleOrNull() ?: 0.0
                        ).result
                        measureMilliliters -> convertLiters.toMilliliters(
                            milliliters = state.liters.toDoubleOrNull() ?: 0.0
                        ).result
                        measureMetricCups -> convertLiters.toMetricCups(
                            cups = state.liters.toDoubleOrNull() ?: 0.0
                        ).result
                        measureGallons -> convertLiters.toGallons(
                            gallons = state.liters.toDoubleOrNull() ?: 0.0
                        ).result
                        measureCubicMeters -> convertLiters.toCubicMeters(
                            meters = state.liters.toDoubleOrNull() ?: 0.0
                        ).result
                        measureCubicInches -> convertLiters.toCubicInches(
                            inches = state.liters.toDoubleOrNull() ?: 0.0
                        ).result
                        else -> state.liters.toDoubleOrNull() ?: 0.0
                    },
                    minPh =
                    when (state.alkalinityMeasure) {
                        measureDKH -> state.minPh.toDoubleOrNull() ?: 0.0
                        measurePpm -> convertDKH.toPpm(
                            ppm = state.minPh.toDoubleOrNull() ?: 0.0
                        ).result
                        measureMeqL -> convertDKH.toMeqL(
                            meqL = state.minPh.toDoubleOrNull() ?: 0.0
                        ).result
                        else -> state.minPh.toDoubleOrNull() ?: 0.0
                    },
                    maxPh =
                    when (state.alkalinityMeasure) {
                        measureDKH -> state.maxPh.toDoubleOrNull() ?: 0.0
                        measurePpm -> convertDKH.toPpm(
                            ppm = state.maxPh.toDoubleOrNull() ?: 0.0
                        ).result
                        measureMeqL -> convertDKH.toMeqL(
                            meqL = state.maxPh.toDoubleOrNull() ?: 0.0
                        ).result
                        else -> state.maxPh.toDoubleOrNull() ?: 0.0
                    },
                    minGh =
                    when (state.alkalinityMeasure) {
                        measureDKH -> state.minGh.toDoubleOrNull() ?: 0.0
                        measurePpm -> convertDKH.toPpm(
                            ppm = state.minGh.toDoubleOrNull() ?: 0.0
                        ).result
                        measureMeqL -> convertDKH.toMeqL(
                            meqL = state.minGh.toDoubleOrNull() ?: 0.0
                        ).result
                        else -> state.minGh.toDoubleOrNull() ?: 0.0
                    },
                    maxGh =
                    when (state.alkalinityMeasure) {
                        measureDKH -> state.maxGh.toDoubleOrNull() ?: 0.0
                        measurePpm -> convertDKH.toPpm(
                            ppm = state.maxGh.toDoubleOrNull() ?: 0.0
                        ).result
                        measureMeqL -> convertDKH.toMeqL(
                            meqL = state.maxGh.toDoubleOrNull() ?: 0.0
                        ).result
                        else -> state.maxGh.toDoubleOrNull() ?: 0.0
                    },
                    minKh =
                    when (state.alkalinityMeasure) {
                        measureDKH -> state.minKh.toDoubleOrNull() ?: 0.0
                        measurePpm -> convertDKH.toPpm(
                            ppm = state.minKh.toDoubleOrNull() ?: 0.0
                        ).result
                        measureMeqL -> convertDKH.toMeqL(
                            meqL = state.minKh.toDoubleOrNull() ?: 0.0
                        ).result
                        else -> state.minKh.toDoubleOrNull() ?: 0.0
                    },
                    maxKh =
                    when (state.alkalinityMeasure) {
                        measureDKH -> state.maxKh.toDoubleOrNull() ?: 0.0
                        measurePpm -> convertDKH.toPpm(
                            ppm = state.maxKh.toDoubleOrNull() ?: 0.0
                        ).result
                        measureMeqL -> convertDKH.toMeqL(
                            meqL = state.maxKh.toDoubleOrNull() ?: 0.0
                        ).result
                        else -> state.maxKh.toDoubleOrNull() ?: 0.0
                    },
                    description = state.description
                )
            )

            insert(state.dweller)
            validationEventChannel.send(ValidationEvent.Success)
        }

    }

    sealed class ValidationEvent {
        object Success : ValidationEvent()
    }

}