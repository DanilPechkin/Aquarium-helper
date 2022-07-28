package com.danilp.aquariumhelper.presentation.screens.in_aquairum.dweller.edit

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.danilp.aquariumhelper.R
import com.danilp.aquariumhelper.presentation.navigation.nav_graphs.InAquariumNavGraph
import com.danilp.aquariumhelper.presentation.screens.FromToInfoFields
import com.danilp.aquariumhelper.presentation.screens.InfoFieldWithError
import com.danilp.aquariumhelper.presentation.screens.destinations.DwellersListDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@OptIn(ExperimentalMaterial3Api::class)
@InAquariumNavGraph
@Destination
@Composable
fun DwellerEdit(
    dwellerId: Int,
    navigator: DestinationsNavigator,
    viewModel: DwellerEditViewModel = hiltViewModel()
) {
    val state = viewModel.state

    LaunchedEffect(key1 = LocalContext.current) {
        viewModel.validationEvents.collect { event ->
            when (event) {
                is DwellerEditViewModel.ValidationEvent.Success -> {
                    navigator.navigate(DwellersListDestination)
                }
            }
        }
    }

    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.edit_dweller_title),
                        maxLines = 1
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { navigator.navigateUp() }
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.ArrowBack,
                            contentDescription = stringResource(R.string.content_description_back_arrow)
                        )
                    }
                }
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

            InfoFieldWithError(
                value = state.name,
                onValueChange = { viewModel.onEvent(DwellerEditEvent.NameChanged(it)) },
                label = stringResource(R.string.name_label),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                errorMessage = state.nameError,
                maxLines = 1,
                singleLine = true,
                textFielModifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            InfoFieldWithError(
                value = state.amount,
                onValueChange = { viewModel.onEvent(DwellerEditEvent.AmountChanged(it)) },
                label = stringResource(R.string.amount_label),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                errorMessage = state.amountError,
                maxLines = 1,
                singleLine = true,
                textFielModifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            InfoFieldWithError(
                value = state.liters,
                onValueChange = { viewModel.onEvent(DwellerEditEvent.LitersChanged(it)) },
                label = stringResource(R.string.capacity_label),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                errorMessage = state.litersError,
                maxLines = 1,
                singleLine = true,
                textFielModifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            FromToInfoFields(
                label = stringResource(R.string.temperature_label),
                valueFrom = state.minTemperature,
                valueTo = state.maxTemperature,
                onValueFromChange = { viewModel.onEvent(DwellerEditEvent.MinTemperatureChanged(it)) },
                onValueToChange = { viewModel.onEvent(DwellerEditEvent.MaxTemperatureChanged(it)) },
                errorMessageFrom = state.minTemperatureError,
                errorMessageTo = state.maxTemperatureError
            )

            Spacer(modifier = Modifier.height(16.dp))

            FromToInfoFields(
                label = stringResource(R.string.ph_label),
                valueFrom = state.minPh,
                valueTo = state.maxPh,
                onValueFromChange = { viewModel.onEvent(DwellerEditEvent.MinPhChanged(it)) },
                onValueToChange = { viewModel.onEvent(DwellerEditEvent.MaxPhChanged(it)) },
                errorMessageFrom = state.minPhError,
                errorMessageTo = state.maxPhError
            )

            Spacer(modifier = Modifier.height(16.dp))

            FromToInfoFields(
                label = stringResource(R.string.gh_label),
                valueFrom = state.minGh,
                valueTo = state.maxGh,
                onValueFromChange = { viewModel.onEvent(DwellerEditEvent.MinGhChanged(it)) },
                onValueToChange = { viewModel.onEvent(DwellerEditEvent.MaxGhChanged(it)) },
                errorMessageFrom = state.minGhError,
                errorMessageTo = state.maxGhError
            )

            Spacer(modifier = Modifier.height(16.dp))

            FromToInfoFields(
                label = stringResource(R.string.kh_label),
                valueFrom = state.minKh,
                valueTo = state.maxKh,
                onValueFromChange = { viewModel.onEvent(DwellerEditEvent.MinKhChanged(it)) },
                onValueToChange = { viewModel.onEvent(DwellerEditEvent.MaxKhChanged(it)) },
                errorMessageFrom = state.minKhError,
                errorMessageTo = state.maxKhError
            )

            Spacer(modifier = Modifier.height(16.dp))

            InfoFieldWithError(
                value = state.description,
                onValueChange = { viewModel.onEvent(DwellerEditEvent.DescriptionChanged(it)) },
                label = stringResource(R.string.description_label),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                textFielModifier = Modifier.fillMaxWidth()
            )

            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    onClick = {
                        viewModel.onEvent(DwellerEditEvent.DeleteButtonPressed)
                    }
                ) {
                    Text(text = stringResource(R.string.delete_button))
                }
                Spacer(modifier = Modifier.width(16.dp))
                Button(
                    onClick = {
                        viewModel.onEvent(DwellerEditEvent.InsertButtonPressed)
                    }
                ) {
                    Text(text = "Save Plant")
                }
            }
        }
    }
}