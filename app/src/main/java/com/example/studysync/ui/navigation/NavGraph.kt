package com.example.studysync.ui.navigation



import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.studysync.ui.auth.login.LoginScreen
import com.example.studysync.ui.auth.signup.SignUpScreen
import com.example.studysync.ui.auth.forgot_password.ForgotPasswordScreen
import com.example.studysync.ui.auth.home.HomeScreen

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screens.Login.route
    ) {
        composable(route = Screens.Login.route) {
            LoginScreen(
                onNavigateToSignUp = { navController.navigate(Screens.SignUp.route) },
                onNavigateToForgotPassword = { navController.navigate(Screens.ForgotPassword.route) },
                onNavigateToHome = {
                    navController.navigate(Screens.Home.route) {
                        popUpTo(Screens.Login.route) { inclusive = true }
                    }
                }
            )
        }

        composable(route = Screens.SignUp.route) {
            SignUpScreen(
                onNavigateBack = { navController.navigateUp() },
                onSignUpSuccess = {
                    navController.navigate(Screens.Home.route) {
                        popUpTo(Screens.Login.route) { inclusive = true }
                    }
                }
            )
        }

        composable(route = Screens.ForgotPassword.route) {
            ForgotPasswordScreen(
                onNavigateBack = { navController.navigateUp() }
            )
        }

        composable(route = Screens.Home.route) {
            HomeScreen()
        }
    }
}