package com.danilp.aquariumhelper.domain.use_case.calculation.capacity

import com.danilp.aquariumhelper.domain.use_case.calculation.CalculationResult
import javax.inject.Singleton

/**
 * Using level Metric Cups
 */
@Singleton
class CalculateLitersMetricCups {
    /**
     * @param liters to calculate cups
     * @param cups to calculate liters
     */
    fun execute(liters: Double = 0.0, cups: Double = 0.0): CalculationResult =
        if (cups == 0.0)
            CalculationResult(result = liters * 3.52)
        else
            CalculationResult(result = cups / 3.52)
}