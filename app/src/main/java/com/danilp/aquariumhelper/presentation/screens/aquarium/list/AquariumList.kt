package com.danilp.aquariumhelper.presentation.screens.aquarium.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.danilp.aquariumhelper.R
import com.danilp.aquariumhelper.presentation.screens.AquariumTopBarWithSearch
import com.danilp.aquariumhelper.presentation.screens.destinations.AccountScreenDestination
import com.danilp.aquariumhelper.presentation.screens.destinations.AquariumEditDestination
import com.danilp.aquariumhelper.presentation.screens.destinations.MainAquariumScreenDestination
import com.danilp.aquariumhelper.presentation.screens.destinations.SettingsScreenDestination
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@OptIn(ExperimentalMaterial3Api::class)
@RootNavGraph
@Destination
@Composable
fun AquariumList(
    navigator: DestinationsNavigator,
    viewModel: AquariumListViewModel = hiltViewModel()
) {
    val state = viewModel.state

    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = state.isRefreshing)
    var isSearchFieldVisible by remember { mutableStateOf(false) }
    val searchFieldFocusRequester = remember { FocusRequester() }
    var isTopMenuExpanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            AquariumTopBarWithSearch(
                stringResource(R.string.aquariums_top_bar_title),
                searchQuery = state.searchQuery,
                onSearchQueryChange = {
                    viewModel.onEvent(AquariumListEvent.OnSearchQueryChange(it))
                },
                isSearchFieldVisible = isSearchFieldVisible,
                switchSearchFieldVisibility = { isSearchFieldVisible = !isSearchFieldVisible },
                hideSearchField = { isSearchFieldVisible = false },
                searchFieldFocusRequester = searchFieldFocusRequester,
                switchMenuVisibility = { isTopMenuExpanded = !isTopMenuExpanded },
                isMenuExpanded = isTopMenuExpanded,
                hideMenu = { isTopMenuExpanded = false },
                navigateBack = { navigator.navigateUp() },
                navigateToSettings = { navigator.navigate(SettingsScreenDestination()) },
                navigateToAccount = { navigator.navigate(AccountScreenDestination()) }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                containerColor = MaterialTheme.colorScheme.primary,
                onClick = {
                    navigator.navigate(
                        AquariumEditDestination(0)
                    )
                }
            ) {
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = stringResource(R.string.add_aquarium_fab)
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues)
        ) {

            SwipeRefresh(
                state = swipeRefreshState,
                onRefresh = { viewModel.onEvent(AquariumListEvent.Refresh) }
            ) {
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(128.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(state.aquariums) { aquarium ->
                        AquariumListItem(
                            aquarium = aquarium,
                            modifier = Modifier
                                .clickable {
                                    viewModel.onEvent(
                                        AquariumListEvent.OnAquariumClicked(aquarium.id)
                                    )
                                    navigator.navigate(
                                        MainAquariumScreenDestination()
                                    )
                                }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}