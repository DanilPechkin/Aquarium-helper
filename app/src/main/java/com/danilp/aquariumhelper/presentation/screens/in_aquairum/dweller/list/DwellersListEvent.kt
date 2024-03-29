package com.danilp.aquariumhelper.presentation.screens.in_aquairum.dweller.list

sealed interface DwellersListEvent {
    object Refresh : DwellersListEvent
    data class OnSearchQueryChange(val query: String) : DwellersListEvent
}
