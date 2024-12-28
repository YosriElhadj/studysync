package com.example.studysync.ui.navigation

sealed class Screens(val route: String) {
    object Login : Screens("login")
    object SignUp : Screens("signup")
    object ForgotPassword : Screens("forgotPassword")
    object Home : Screens("home")
}