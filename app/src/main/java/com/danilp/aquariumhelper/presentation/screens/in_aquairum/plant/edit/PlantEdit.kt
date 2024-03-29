package com.danilp.aquariumhelper.presentation.screens.in_aquairum.plant.edit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.danilp.aquariumhelper.R
import com.danilp.aquariumhelper.presentation.navigation.nav_graphs.InAquariumNavGraph
import com.danilp.aquariumhelper.presentation.screens.AquariumTopBar
import com.danilp.aquariumhelper.presentation.screens.FromToInfoFields
import com.danilp.aquariumhelper.presentation.screens.ImagePicker
import com.danilp.aquariumhelper.presentation.screens.InfoFieldWithError
import com.danilp.aquariumhelper.presentation.screens.destinations.AccountScreenDestination
import com.danilp.aquariumhelper.presentation.screens.destinations.PlantsListDestination
import com.danilp.aquariumhelper.presentation.screens.destinations.SettingsScreenDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@OptIn(ExperimentalMaterial3Api::class)
@InAquariumNavGraph
@Destination
@Composable
fun PlantEdit(
    plantId: Int,
    navigator: DestinationsNavigator,
    viewModel: PlantEditViewModel = hiltViewModel()
) {
    val state = viewModel.state

    var isTopMenuExpanded by remember { mutableStateOf(false) }


    LaunchedEffect(key1 = LocalContext.current) {
        viewModel.validationEvents.collect { event ->
            when (event) {
                is PlantEditViewModel.ValidationEvent.Success -> {
                    navigator.navigate(PlantsListDestination)
                }
            }
        }
    }

    Scaffold(
        topBar = {
            AquariumTopBar(
                stringResource(R.string.edit_plant_title),
                switchMenuVisibility = { isTopMenuExpanded = !isTopMenuExpanded },
                isMenuExpanded = isTopMenuExpanded,
                hideMenu = { isTopMenuExpanded = false },
                navigateBack = { navigator.navigateUp() },
                navigateToSettings = { navigator.navigate(SettingsScreenDestination()) },
                navigateToAccount = { navigator.navigate(AccountScreenDestination()) }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {

            val focusManager = LocalFocusManager.current

            ImagePicker(
                imageUri = state.plant.imageUri,
                onSelection = { viewModel.onEvent(PlantEditEvent.ImagePicked(it)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 256.dp)
                    .clip(RoundedCornerShape(8.dp))
            )

            Spacer(modifier = Modifier.height(16.dp))

            InfoFieldWithError(
                value = state.name,
                onValueChange = { viewModel.onEvent(PlantEditEvent.NameChanged(it)) },
                label = stringResource(R.string.name_label),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        focusManager.moveFocus(FocusDirection.Next)
                    }
                ),
                error = state.nameErrorCode,
                maxLines = 1,
                singleLine = true,
                textFieldModifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            FromToInfoFields(
                label = stringResource(R.string.temperature_label),
                valueFrom = state.minTemperature,
                valueTo = state.maxTemperature,
                onValueFromChange = { viewModel.onEvent(PlantEditEvent.MinTemperatureChanged(it)) },
                onValueToChange = { viewModel.onEvent(PlantEditEvent.MaxTemperatureChanged(it)) },
                keyboardActionsFrom = KeyboardActions(
                    onNext = {
                        focusManager.moveFocus(FocusDirection.Next)
                    }
                ),
                keyboardActionsTo = KeyboardActions(
                    onNext = {
                        focusManager.moveFocus(FocusDirection.Next)
                    }
                ),
                errorFrom = state.minTemperatureError,
                errorTo = state.maxTemperatureError
            )

            Spacer(modifier = Modifier.height(16.dp))

            FromToInfoFields(
                label = stringResource(R.string.ph_label),
                valueFrom = state.minPh,
                valueTo = state.maxPh,
                onValueFromChange = { viewModel.onEvent(PlantEditEvent.MinPhChanged(it)) },
                onValueToChange = { viewModel.onEvent(PlantEditEvent.MaxPhChanged(it)) },
                keyboardActionsFrom = KeyboardActions(
                    onNext = {
                        focusManager.moveFocus(FocusDirection.Next)
                    }
                ),
                keyboardActionsTo = KeyboardActions(
                    onNext = {
                        focusManager.moveFocus(FocusDirection.Next)
                    }
                ),
                errorFrom = state.minPhError,
                errorTo = state.maxPhError
            )

            Spacer(modifier = Modifier.height(16.dp))

            FromToInfoFields(
                label = stringResource(R.string.gh_label),
                valueFrom = state.minGh,
                valueTo = state.maxGh,
                onValueFromChange = { viewModel.onEvent(PlantEditEvent.MinGhChanged(it)) },
                onValueToChange = { viewModel.onEvent(PlantEditEvent.MaxGhChanged(it)) },
                keyboardActionsFrom = KeyboardActions(
                    onNext = {
                        focusManager.moveFocus(FocusDirection.Next)
                    }
                ),
                keyboardActionsTo = KeyboardActions(
                    onNext = {
                        focusManager.moveFocus(FocusDirection.Next)
                    }
                ),
                errorFrom = state.minGhError,
                errorTo = state.maxGhError
            )

            Spacer(modifier = Modifier.height(16.dp))

            FromToInfoFields(
                label = stringResource(R.string.kh_label),
                valueFrom = state.minKh,
                valueTo = state.maxKh,
                onValueFromChange = { viewModel.onEvent(PlantEditEvent.MinKhChanged(it)) },
                onValueToChange = { viewModel.onEvent(PlantEditEvent.MaxKhChanged(it)) },
                keyboardActionsFrom = KeyboardActions(
                    onNext = {
                        focusManager.moveFocus(FocusDirection.Next)
                    }
                ),
                keyboardActionsTo = KeyboardActions(
                    onNext = {
                        focusManager.moveFocus(FocusDirection.Next)
                    }
                ),
                errorFrom = state.minKhError,
                errorTo = state.maxKhError
            )

            Spacer(modifier = Modifier.height(16.dp))

            InfoFieldWithError(
                value = state.minCO2,
                onValueChange = { viewModel.onEvent(PlantEditEvent.MinCO2Changed(it)) },
                label = stringResource(R.string.min_co2_label),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        focusManager.moveFocus(FocusDirection.Next)
                    }
                ),
                error = state.minCO2Error,
                maxLines = 1,
                singleLine = true,
                textFieldModifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            InfoFieldWithError(
                value = state.minIllumination,
                onValueChange = { viewModel.onEvent(PlantEditEvent.MinIlluminationChanged(it)) },
                label = stringResource(R.string.illumination_label),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        focusManager.moveFocus(FocusDirection.Next)
                    }
                ),
                error = state.minIlluminationError,
                maxLines = 1,
                singleLine = true,
                textFieldModifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = state.description,
                onValueChange = { viewModel.onEvent(PlantEditEvent.DescriptionChanged(it)) },
                label = {
                    Text(text = stringResource(R.string.description_label))
                },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                    }
                )
            )
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                OutlinedButton(
                    onClick = {
                        viewModel.onEvent(PlantEditEvent.DeleteButtonPressed)
                    }
                ) {
                    Text(text = stringResource(R.string.delete_button))
                }
                Spacer(modifier = Modifier.width(16.dp))
                Button(
                    onClick = {
                        viewModel.onEvent(PlantEditEvent.InsertButtonPressed)
                    }
                ) {
                    Text(text = stringResource(R.string.save_button))
                }
            }
        }
    }
}