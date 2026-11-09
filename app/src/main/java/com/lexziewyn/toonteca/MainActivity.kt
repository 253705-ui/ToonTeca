package com.lexziewyn.toonteca

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.lexziewyn.toonteca.data.remote.RetrofitInstance
import com.lexziewyn.toonteca.ui.auth.AuthViewModel
import com.lexziewyn.toonteca.ui.auth.LoginScreen
import com.lexziewyn.toonteca.ui.auth.RegisterScreen
import com.lexziewyn.toonteca.ui.comics.ComicDetailScreen
import com.lexziewyn.toonteca.ui.comics.ComicFormScreen
import com.lexziewyn.toonteca.ui.comics.ComicListScreen
import com.lexziewyn.toonteca.ui.comics.WeebcomicViewModel
import com.lexziewyn.toonteca.ui.theme.ToonTecaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val app = application as ToonTecaApplication
        val container = app.container

        setContent {
            ToonTecaTheme {
                val navController = rememberNavController()
                val authViewModel: AuthViewModel = viewModel()
                val comicViewModel: WeebcomicViewModel = viewModel(
                    factory = WeebcomicViewModel.provideFactory(container.weebcomicRepository)
                )

                val startDestination = if (container.sessionManager.isLoggedIn()) "home" else "login"

                NavHost(navController = navController, startDestination = startDestination) {
                    composable("login") {
                        LoginScreen(
                            viewModel = authViewModel,
                            onLoginSuccess = {
                                container.sessionManager.saveToken(RetrofitInstance.token)
                                comicViewModel.loadComics()
                                navController.navigate("home") {
                                    popUpTo("login") { inclusive = true }
                                }
                            },
                            onNavigateToRegister = {
                                navController.navigate("register")
                            }
                        )
                    }

                    composable("register") {
                        RegisterScreen(
                            viewModel = authViewModel,
                            onRegisterSuccess = {
                                container.sessionManager.saveToken(RetrofitInstance.token)
                                comicViewModel.loadComics()
                                navController.navigate("home") {
                                    popUpTo("login") { inclusive = true }
                                }
                            },
                            onNavigateToLogin = {
                                navController.popBackStack()
                            }
                        )
                    }

                    composable("home") {
                        ComicListScreen(
                            viewModel = comicViewModel,
                            onComicClick = { comicId ->
                                navController.navigate("detail/$comicId")
                            },
                            onAddClick = {
                                navController.navigate("form")
                            },
                            onLogoutClick = {
                                container.sessionManager.clearToken()
                                RetrofitInstance.token = ""
                                navController.navigate("login") {
                                    popUpTo("home") { inclusive = true }
                                }
                            }
                        )
                    }

                    composable("form") {
                        ComicFormScreen(
                            comicId = null,
                            viewModel = comicViewModel,
                            onBack = { navController.popBackStack() }
                        )
                    }

                    composable(
                        route = "detail/{id}",
                        arguments = listOf(navArgument("id") { type = NavType.IntType })
                    ) { backStackEntry ->
                        val id = backStackEntry.arguments?.getInt("id") ?: 0
                        ComicDetailScreen(
                            comicId = id,
                            viewModel = comicViewModel,
                            onBack = { navController.popBackStack() },
                            onEditClick = { editId ->
                                navController.navigate("form_edit/$editId")
                            }
                        )
                    }

                    composable(
                        route = "form_edit/{id}",
                        arguments = listOf(navArgument("id") { type = NavType.IntType })
                    ) { backStackEntry ->
                        val id = backStackEntry.arguments?.getInt("id") ?: 0
                        ComicFormScreen(
                            comicId = id,
                            viewModel = comicViewModel,
                            onBack = { navController.popBackStack() }
                        )
                    }
                }
            }
        }
    }
}
