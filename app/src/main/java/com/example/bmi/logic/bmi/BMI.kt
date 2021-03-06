package com.example.bmi.logic.bmi

class BMI(var mass: Int, var height: Int, var usingImperialUnits: Boolean) {
    private fun fromKgCm(): Double {
        require(mass > 0) { "Mass must be greater than 0" }
        require(height > 0) { "Height must be greater than 0" }

        return mass * 10000.0 / (height * height)
    }

    private fun fromLbsInch(): Double {
        require(mass > 0) { "Mass must be greater than 0" }
        require(height > 0) { "Height must be greater than 0" }

        return 703.0 * mass / (height * height)
    }

    fun countBMI(): Double = if (usingImperialUnits) fromLbsInch() else fromKgCm()

    fun getCategory(): BmiCategory {
        val result = countBMI()
        return when {
            result < 18.5 -> BmiCategory.UNDERWEIGHT
            result < 25 -> BmiCategory.NORMAL
            result < 30 -> BmiCategory.OVERWEIGHT
            result >= 30 -> BmiCategory.OBESE
            else -> BmiCategory.NA
        }
    }
}