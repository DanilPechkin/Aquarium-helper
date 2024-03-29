package com.danilp.aquariumhelper.presentation.screens.aquarium.aquariums_screen

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.danilp.aquariumhelper.presentation.screens.NavGraphs
import com.danilp.aquariumhelper.presentation.screens.appCurrentDestinationAsState
import com.danilp.aquariumhelper.presentation.screens.destinations.Destination
import com.danilp.aquariumhelper.presentation.screens.in_aquairum.in_aquarium_bottom_bar.AquariumBottomBar
import com.danilp.aquariumhelper.presentation.screens.in_aquairum.in_aquarium_bottom_bar.AquariumBottomBarDestination
import com.danilp.aquariumhelper.presentation.screens.startAppDestination
import com.ramcosta.composedestinations.DestinationsNavHost

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AquariumsScreen() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            val currentDestination: Destination = navController.appCurrentDestinationAsState().value
                ?: NavGraphs.inAquarium.startAppDestination

            if (AquariumBottomBarDestination.values().any { it.direction == currentDestination }) {
                AquariumBottomBar(
                    navController = navController,
                    currentDestination = currentDestination
                )
            }
        }
    ) { paddingValues ->
        DestinationsNavHost(
            navController = navController,
            navGraph = NavGraphs.root,
            modifier = Modifier.padding(paddingValues)
        )
    }
}