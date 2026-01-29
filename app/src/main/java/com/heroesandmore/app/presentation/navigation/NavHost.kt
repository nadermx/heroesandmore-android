package com.heroesandmore.app.presentation.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Collections
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Collections
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.heroesandmore.app.R
import com.heroesandmore.app.presentation.screens.auth.LoginScreen
import com.heroesandmore.app.presentation.screens.auth.RegisterScreen
import com.heroesandmore.app.presentation.screens.browse.BrowseScreen
import com.heroesandmore.app.presentation.screens.collections.CollectionsScreen
import com.heroesandmore.app.presentation.screens.collections.CollectionDetailScreen
import com.heroesandmore.app.presentation.screens.home.HomeScreen
import com.heroesandmore.app.presentation.screens.listing.ListingDetailScreen
import com.heroesandmore.app.presentation.screens.listing.CreateListingScreen
import com.heroesandmore.app.presentation.screens.profile.ProfileScreen
import com.heroesandmore.app.presentation.screens.search.SearchScreen

data class BottomNavItem(
    val route: String,
    val titleResId: Int,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

val bottomNavItems = listOf(
    BottomNavItem(
        route = Screen.Home.route,
        titleResId = R.string.nav_home,
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home
    ),
    BottomNavItem(
        route = Screen.Browse.route,
        titleResId = R.string.nav_browse,
        selectedIcon = Icons.Filled.Search,
        unselectedIcon = Icons.Outlined.Search
    ),
    BottomNavItem(
        route = Screen.Sell.route,
        titleResId = R.string.nav_sell,
        selectedIcon = Icons.Filled.Add,
        unselectedIcon = Icons.Filled.Add
    ),
    BottomNavItem(
        route = Screen.Collections.route,
        titleResId = R.string.nav_collections,
        selectedIcon = Icons.Filled.Collections,
        unselectedIcon = Icons.Outlined.Collections
    ),
    BottomNavItem(
        route = Screen.Profile.route,
        titleResId = R.string.nav_profile,
        selectedIcon = Icons.Filled.Person,
        unselectedIcon = Icons.Outlined.Person
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HeroesNavHost(
    navController: NavHostController = rememberNavController()
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    // Routes that should show bottom nav
    val showBottomNav = currentDestination?.route in listOf(
        Screen.Home.route,
        Screen.Browse.route,
        Screen.Sell.route,
        Screen.Collections.route,
        Screen.Profile.route
    )

    Scaffold(
        bottomBar = {
            if (showBottomNav) {
                NavigationBar {
                    bottomNavItems.forEach { item ->
                        val selected = currentDestination?.hierarchy?.any { it.route == item.route } == true
                        NavigationBarItem(
                            icon = {
                                Icon(
                                    imageVector = if (selected) item.selectedIcon else item.unselectedIcon,
                                    contentDescription = stringResource(item.titleResId)
                                )
                            },
                            label = { Text(stringResource(item.titleResId)) },
                            selected = selected,
                            onClick = {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            // Main tabs
            composable(Screen.Home.route) {
                HomeScreen(navController = navController)
            }

            composable(Screen.Browse.route) {
                BrowseScreen(navController = navController)
            }

            composable(Screen.Sell.route) {
                CreateListingScreen(navController = navController)
            }

            composable(Screen.Collections.route) {
                CollectionsScreen(navController = navController)
            }

            composable(Screen.Profile.route) {
                ProfileScreen(navController = navController)
            }

            // Auth
            composable(Screen.Login.route) {
                LoginScreen(navController = navController)
            }

            composable(Screen.Register.route) {
                RegisterScreen(navController = navController)
            }

            // Listing detail
            composable(
                route = Screen.ListingDetail.route,
                arguments = listOf(navArgument("listingId") { type = NavType.IntType })
            ) { backStackEntry ->
                val listingId = backStackEntry.arguments?.getInt("listingId") ?: 0
                ListingDetailScreen(
                    listingId = listingId,
                    navController = navController
                )
            }

            // Collection detail
            composable(
                route = Screen.CollectionDetail.route,
                arguments = listOf(navArgument("collectionId") { type = NavType.IntType })
            ) { backStackEntry ->
                val collectionId = backStackEntry.arguments?.getInt("collectionId") ?: 0
                CollectionDetailScreen(
                    collectionId = collectionId,
                    navController = navController
                )
            }

            // Search
            composable(
                route = Screen.Search.route,
                arguments = listOf(navArgument("query") { defaultValue = "" })
            ) { backStackEntry ->
                val query = backStackEntry.arguments?.getString("query") ?: ""
                SearchScreen(
                    initialQuery = query,
                    navController = navController
                )
            }

            // Category browsing
            composable(
                route = Screen.Category.route,
                arguments = listOf(navArgument("slug") { type = NavType.StringType })
            ) { backStackEntry ->
                val slug = backStackEntry.arguments?.getString("slug") ?: ""
                BrowseScreen(
                    categorySlug = slug,
                    navController = navController
                )
            }
        }
    }
}
