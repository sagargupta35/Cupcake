/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.cupcake

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.cupcake.ui.OrderViewModel
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.cupcake.data.DataSource
import com.example.cupcake.ui.OrderSummaryScreen
import com.example.cupcake.ui.SelectOptionScreen
import com.example.cupcake.ui.StartOrderScreen
import com.example.cupcake.ui.theme.LoginScreen
import com.example.cupcake.ui.theme.LoginViewModel
import com.example.cupcake.ui.theme.SignUpScreen

/**
 * Composable that displays the topBar and displays back button if back navigation is possible.
 */
@Composable
fun CupcakeAppBar(
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    currentScreent: CupcakeScreen,
    onLogOut: () -> Unit,
    canLogOut: Boolean,
) {
    TopAppBar(
        title = { Text(stringResource(currentScreent.title)) },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack && currentScreent.name != "Start") {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button)
                    )
                }
            }
        },

        actions = {
            if (canLogOut) {
                IconButton(onClick = onLogOut) {
                    Icon(Icons.Filled.ExitToApp, contentDescription = null)
                }
            }
        }

    )
}

enum class CupcakeScreen(@StringRes val title: Int){
    Start(title = R.string.app_name),
    Flavour(title = R.string.choose_flavor),
    Pickup(title = R.string.choose_pickup_date),
    Summary(title = R.string.order_summary),
    SignUp(title = R.string.signup_screen),
    Login(title = R.string.login_screen)
}

@Composable
fun CupcakeApp(
    viewModel: OrderViewModel = viewModel(),
    navController: NavHostController = rememberNavController(),
    loginViewModel: LoginViewModel = viewModel()
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = CupcakeScreen.valueOf(backStackEntry?.destination?.route ?: CupcakeScreen.Start.name)

    Scaffold(
        topBar = {
            CupcakeAppBar(
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() },
                currentScreent = currentScreen,
                onLogOut = {
                    loginViewModel.logOut()
                    navController.popBackStack(CupcakeScreen.SignUp.name, inclusive = false)
                },
                canLogOut = loginViewModel.loggedIn
            )
        }
    ) { innerPadding ->
        val uiState by viewModel.uiState.collectAsState()

        NavHost(
            navController = navController,
            startDestination = CupcakeScreen.SignUp.name,
            modifier = Modifier.padding(innerPadding),
        ){
            composable(route = CupcakeScreen.SignUp.name){
                SignUpScreen(textFieldDetails = DataSource.textFieldData,
                    viewModel = loginViewModel,
                    onLoginClick = {
                        loginViewModel.failure = ""
                        navController.navigate(CupcakeScreen.Login.name)},
                    onRegister = {
                       loginViewModel.createUserInFireBase{
                           navController.navigate(CupcakeScreen.Start.name)
                           navController.popBackStack(CupcakeScreen.Start.name, false)
                       }
                    }
                )

            }

            composable(route = CupcakeScreen.Login.name){
                LoginScreen(textFieldDetails = DataSource.loginFieldData,
                    viewModel = loginViewModel,
                    onRegisterClick = {navController.navigateUp()},
                    onLoginClick = {
                        loginViewModel.logIn{
                            navController.navigate(CupcakeScreen.Start.name)
                            navController.popBackStack(CupcakeScreen.Start.name, false)
                        }
                    }
                )

            }

            composable(route = CupcakeScreen.Start.name){
                StartOrderScreen(quantityOptions = DataSource.quantityOptions,
                    onNextButtonClicked = {
                        viewModel.setQuantity(it)
                        navController.navigate(CupcakeScreen.Flavour.name)
                    }
                )
            }

            composable(route = CupcakeScreen.Flavour.name){
                val context = LocalContext.current
                SelectOptionScreen(subtotal = uiState.price,
                    options = DataSource.flavors.map { context.resources.getString(it) },
                    onSelectionChanged = { viewModel.setFlavor(it) },
                    onNextButtonClicked = { navController.navigate(CupcakeScreen.Pickup.name) },
                    onCancelButtonClicked = {cancelOrderAndNavigateToStart(viewModel, navController)}
                )
            }

            composable(route = CupcakeScreen.Pickup.name){
                SelectOptionScreen(subtotal = uiState.price,
                    options = uiState.pickupOptions,
                    onSelectionChanged = { viewModel.setDate(it) },
                    onNextButtonClicked = { navController.navigate(CupcakeScreen.Summary.name) },
                    modifier = Modifier.fillMaxHeight(),
                    onCancelButtonClicked = { cancelOrderAndNavigateToStart(viewModel, navController) }
                )
            }

            composable(route = CupcakeScreen.Summary.name) {
                val context = LocalContext.current
                OrderSummaryScreen(
                    orderUiState = uiState,
                    onCancelButtonClicked = { cancelOrderAndNavigateToStart(viewModel, navController) },
                    onSendButtonClicked = { subject: String, summary: String ->
                        shareOrder(context = context , subject, subject)
                    },
                    modifier = Modifier.fillMaxHeight()
                )
            }
        }

    }
}

private fun shareOrder(context: Context, subject:String, summary: String){
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_SUBJECT, subject)
        putExtra(Intent.EXTRA_TEXT, subject)
    }

    context.startActivity(
        Intent.createChooser(
            intent, context.resources.getString(R.string.new_cupcake_order)
        )
    )
}

private fun cancelOrderAndNavigateToStart(
    viewModel: OrderViewModel,
    navController: NavHostController
){
    viewModel.resetOrder()
    navController.popBackStack(CupcakeScreen.Start.name, inclusive = false)
}
