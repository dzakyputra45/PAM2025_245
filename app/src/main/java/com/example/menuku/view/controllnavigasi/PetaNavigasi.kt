package com.example.menuku.view.controllNavigasi

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.menuku.repositori.RepositoryMenu
import com.example.menuku.view.*
import com.example.menuku.view.route.*
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun PetaNavigasi() {
    val navController = rememberNavController()

    LaunchedEffect(Unit) {
        android.util.Log.d("NAV_DEBUG", "PetaNavigasi DIPAKAI")
    }

    // 🔥 TAMBAHKAN INI
    val repositoryMenu = RepositoryMenu(
        FirebaseFirestore.getInstance()
    )

    NavHost(
        navController = navController,
        startDestination = DestinasiHome.route
    ) {

        composable(route = DestinasiHome.route) {
            DashboardScreen(navController = navController)
        }

        composable(route = DestinasiEntry.route) {
            TambahMenuScreen(
                onBack = { navController.popBackStack() },
                repository = repositoryMenu
            )
        }

        composable(
            route = DestinasiEdit.route + "?menuId={menuId}",
            arguments = listOf(
                navArgument(DestinasiEdit.MENU_ID) {
                    type = NavType.StringType
                }
            )
        ) {
            EditMenuScreen(
                onBack = { navController.popBackStack() }
            )
        }

    }
}
