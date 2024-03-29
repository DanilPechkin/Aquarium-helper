package com.danilp.aquariumhelper.presentation.screens.top_bar_menu.account.sign_up

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.hilt.navigation.compose.hiltViewModel
import com.danilp.aquariumhelper.R
import com.danilp.aquariumhelper.presentation.screens.AquariumTopBar
import com.danilp.aquariumhelper.presentation.screens.InfoFieldWithError
import com.danilp.aquariumhelper.presentation.screens.NavGraphs
import com.danilp.aquariumhelper.presentation.screens.PasswordFieldWithError
import com.danilp.aquariumhelper.presentation.screens.destinations.AccountScreenDestination
import com.danilp.aquariumhelper.presentation.screens.destinations.AquariumSplashScreenDestination
import com.danilp.aquariumhelper.presentation.screens.destinations.SettingsScreenDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@OptIn(ExperimentalMaterial3Api::class)
@RootNavGraph
@Destination
@Composable
fun SignUpScreen(
    navigator: DestinationsNavigator,
    viewModel: SignUpViewModel = hiltViewModel()
) {
    val state = viewModel.state

    var isTopMenuExpanded by rememberSaveable { mutableStateOf(false) }
    var textFieldSize by remember { mutableStateOf(Size.Zero) }

    var isFirstPasswordVisible by remember { mutableStateOf(false) }
    var isSecondPasswordVisible by remember { mutableStateOf(false) }

    val context = LocalContext.current

    LaunchedEffect(key1 = context) {
        viewModel.signEvents.collect { event ->
            when (event) {
                SignUpViewModel.SignEvent.Success -> {
                    navigator.clearBackStack(NavGraphs.root.startRoute)
                    navigator.navigate(AquariumSplashScreenDestination())
                }
            }
        }
    }

    Scaffold(
        topBar = {
            AquariumTopBar(
                title = stringResource(R.string.signing_up_title),
                switchMenuVisibility = { isTopMenuExpanded = !isTopMenuExpanded },
                isMenuExpanded = isTopMenuExpanded,
                hideMenu = { isTopMenuExpanded = false },
                navigateBack = { navigator.navigateUp() },
                navigateToSettings = { navigator.navigate(SettingsScreenDestination) },
                navigateToAccount = { navigator.navigate(AccountScreenDestination()) }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val focusManager = LocalFocusManager.current

            InfoFieldWithError(
                value = state.email,
                onValueChange = { viewModel.onEvent(SignUpEvent.EmailChanged(it)) },
                label = stringResource(R.string.email_label),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        focusManager.moveFocus(FocusDirection.Next)
                    }
                ),
                maxLines = 1,
                singleLine = true,
                error = state.emailErrorCode
            )

            Spacer(modifier = Modifier.height(16.dp))

            PasswordFieldWithError(
                value = state.password,
                onValueChange = { viewModel.onEvent(SignUpEvent.FirstPasswordChanged(it)) },
                changePasswordVisibility = { isFirstPasswordVisible = !isFirstPasswordVisible },
                isPasswordVisible = isFirstPasswordVisible,
                label = stringResource(R.string.password_label),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        focusManager.moveFocus(
                            FocusDirection.Next
                        )
                    }
                ),
                maxLines = 1,
                singleLine = true,
                error = state.passwordErrorCode
            )

            Spacer(modifier = Modifier.height(16.dp))

            PasswordFieldWithError(
                value = state.repeatedPassword,
                onValueChange = { viewModel.onEvent(SignUpEvent.SecondPasswordChanged(it)) },
                changePasswordVisibility = { isSecondPasswordVisible = !isSecondPasswordVisible },
                isPasswordVisible = isSecondPasswordVisible,
                label = stringResource(R.string.repeat_password_label),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        focusManager.moveFocus(
                            FocusDirection.Next
                        )
                    }
                ),
                maxLines = 1,
                singleLine = true,
                textFieldModifier = Modifier
                    .onGloballyPositioned { coordinates ->
                        textFieldSize = coordinates.size.toSize()
                    },
                error = state.repeatedPasswordErrorCode
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.width(
                    with(LocalDensity.current) {
                        textFieldSize.width.toDp()
                    }
                ),
                horizontalArrangement = Arrangement.End
            ) {
                Button(onClick = { viewModel.onEvent(SignUpEvent.SignUpButtonPressed) }) {
                    Text(
                        text = stringResource(R.string.sign_up_button)
                    )
                }
            }
        }
    }
}