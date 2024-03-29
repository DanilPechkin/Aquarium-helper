package com.danilp.aquariumhelper.presentation.screens.in_aquairum.plant.list

sealed interface PlantsListEvent {
    object Refresh : PlantsListEvent
    data class OnSearchQueryChange(val query: String) : PlantsListEvent
}
