package com.danilp.aquariumhelper.data.plant.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "plants",
    indices = [
        Index("aquariumId"),
        Index("name")
    ]
)
data class PlantEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val aquariumId: Int,
    val imageUri: String,
    val name: String,
    val genus: String,
    @ColumnInfo(name = "min_temperature") val minTemperature: Double,
    @ColumnInfo(name = "max_temperature") val maxTemperature: Double,
    @ColumnInfo(name = "min_ph") val minPh: Double,
    @ColumnInfo(name = "max_ph") val maxPh: Double,
    @ColumnInfo(name = "min_gh") val minGh: Double,
    @ColumnInfo(name = "max_gh") val maxGh: Double,
    @ColumnInfo(name = "min_kh") val minKh: Double,
    @ColumnInfo(name = "max_kh") val maxKh: Double,
    @ColumnInfo(name = "min_co2") val minCO2: Double,
    @ColumnInfo(name = "min_illumination") val minIllumination: Double,
    val description: String
)
