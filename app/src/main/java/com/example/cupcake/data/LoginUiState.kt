package com.example.cupcake.data

data class LoginUiState(
    val isFirstNameValid: Boolean = false,
    val isLastNameValid: Boolean = false,
    val isEmailValid: Boolean = false,
    val isPasswordValid: Boolean = false,
    val isCheckBoxChecked: Boolean = false,
    val canRegister: Boolean = setOf(isEmailValid, isPasswordValid, isFirstNameValid, isCheckBoxChecked).all { it }
)