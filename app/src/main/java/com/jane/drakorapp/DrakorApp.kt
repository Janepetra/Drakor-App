package com.jane.drakorapp

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.jane.drakorapp.navigation.NavigationItem
import com.jane.drakorapp.navigation.Screen
import com.jane.drakorapp.ui.screen.DetailScreen
import com.jane.drakorapp.ui.screen.FavoriteScreen
import com.jane.drakorapp.ui.screen.HomeScreen
import com.jane.drakorapp.ui.screen.ProfileScreen

@Composable
fun DrakorApp(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    //scaffold untuk membuat struktur layout bottom navigasi
    Scaffold (
        bottomBar = {
            if (currentRoute != Screen.Detail.route) {
                BottomBar(navController)
            }
        }
    ) { innerPadding ->
        //navHost berisi semua screen yg di kelola oleh navController
        NavHost(
            navController =  navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) {
                HomeScreen(
                    navigateToDetail = { DrakorId ->
                        navController.navigate(Screen.Detail.createRoute(DrakorId))
                    }
                )
            }
            composable(
                route = Screen.Favorite.route) {
                FavoriteScreen(
                    navigateToDetail = { DrakorId ->
                        navController.navigate(Screen.Detail.createRoute(DrakorId))
                    }
                )
            }
            composable(
                route = Screen.Profile.route) {
                ProfileScreen()
            }
            composable(
                route = Screen.Detail.route,
                //intent dengan argument untuk mengirim data
                arguments = listOf(navArgument("DrakorId") { type = NavType.IntType }),
            ) {
                //get data in detailScreen
                val DrakorId = it.arguments?.getInt("DrakorId") ?: 0
                DetailScreen(
                    DrakorId = DrakorId,
                    navigateBack = {
                        navController.navigateUp()
                    },
                )
            }
        }
    }
}

@Composable
fun BottomBar(
    navController: NavHostController,
    modifier: Modifier =  Modifier
) {
    NavigationBar (
        modifier = modifier,
        ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        val navigationItems = listOf(
            NavigationItem(
                title = stringResource(R.string.menu_home),
                icon = Icons.Default.Home,
                screen = Screen.Home
            ),
            NavigationItem(
                title = stringResource(R.string.menu_fav),
                icon = Icons.Default.Favorite,
                screen = Screen.Favorite
            ),
            NavigationItem(
                title = stringResource(R.string.menu_profile),
                icon = Icons.Default.AccountCircle,
                screen = Screen.Profile
            ),
        )
        navigationItems.map {
            NavigationBarItem (
                icon = {
                    Icon(
                        imageVector = it.icon,
                        contentDescription = it.title
                    )
                },
                label = {Text (it.title)},
                selected = currentRoute == it.screen.route,
                onClick = {
                    //navController.navigate: digunakan untuk eksekusi navigasi
                    // ke route sesuai dengan item yang dipilih.
                    navController.navigate(it.screen.route) {
                        //popUpTo: digunakan untuk kembali ke halaman awal
                        // supaya tidak membuka halaman baru terus menerus.
                        popUpTo(navController.graph.findStartDestination().id) {
                            //saveState dan restoreState: mengembalikan state ketika
                            // item dipilih lagi.
                            saveState = true
                        }
                        restoreState = true
                        //launchSingleTop: digunakan supaya tidak ada halaman yang dobel
                        // ketika memilih ulang item yang sama.
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}