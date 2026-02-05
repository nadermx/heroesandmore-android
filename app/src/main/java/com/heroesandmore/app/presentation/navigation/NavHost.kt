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
                HomeScreen(
                    onNavigateToSearch = { navController.navigate(Screen.Search.createRoute("")) },
                    onNavigateToListing = { listingId -> navController.navigate(Screen.ListingDetail.createRoute(listingId)) },
                    onNavigateToCategory = { slug -> navController.navigate(Screen.Category.createRoute(slug)) }
                )
            }

            composable(Screen.Browse.route) {
                BrowseScreen(
                    onNavigateToCategory = { slug -> navController.navigate(Screen.Category.createRoute(slug)) },
                    onNavigateToSearch = { navController.navigate(Screen.Search.createRoute("")) }
                )
            }

            composable(Screen.Sell.route) {
                CreateListingScreen(
                    onNavigateBack = { navController.popBackStack() },
                    onListingCreated = { listingId -> 
                        navController.navigate(Screen.ListingDetail.createRoute(listingId)) {
                            popUpTo(Screen.Home.route)
                        }
                    }
                )
            }

            composable(Screen.Collections.route) {
                CollectionsScreen(
                    onNavigateToLogin = { navController.navigate(Screen.Login.route) },
                    onNavigateToCollection = { collectionId -> navController.navigate(Screen.CollectionDetail.createRoute(collectionId)) },
                    onNavigateToCreateCollection = { /* Show dialog or navigate to create screen */ }
                )
            }

            composable(Screen.Profile.route) {
                ProfileScreen(
                    onNavigateToLogin = { navController.navigate(Screen.Login.route) },
                    onNavigateToSettings = { /* Navigate to settings */ },
                    onNavigateToMyListings = { /* Navigate to my listings */ },
                    onNavigateToMyOrders = { /* Navigate to my orders */ },
                    onNavigateToSavedListings = { /* Navigate to saved */ },
                    onNavigateToMessages = { /* Navigate to messages */ },
                    onNavigateToNotifications = { /* Navigate to notifications */ },
                    onNavigateToWishlists = { /* Navigate to wishlists */ },
                    onNavigateToPriceAlerts = { /* Navigate to price alerts */ }
                )
            }

            // Auth
            composable(Screen.Login.route) {
                LoginScreen(
                    onNavigateToRegister = { navController.navigate(Screen.Register.route) },
                    onNavigateToForgotPassword = { /* Navigate to forgot password */ },
                    onLoginSuccess = { navController.popBackStack() }
                )
            }

            composable(Screen.Register.route) {
                RegisterScreen(
                    onNavigateToLogin = { navController.popBackStack() },
                    onRegisterSuccess = { 
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    }
                )
            }

            // Listing detail
            composable(
                route = Screen.ListingDetail.route,
                arguments = listOf(navArgument("listingId") { type = NavType.IntType })
            ) { backStackEntry ->
                val listingId = backStackEntry.arguments?.getInt("listingId") ?: 0
                ListingDetailScreen(
                    listingId = listingId,
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToSeller = { username -> /* Navigate to seller profile */ },
                    onNavigateToCheckout = { id -> /* Navigate to checkout */ }
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
                    onNavigateBack = { navController.popBackStack() }
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
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToListing = { listingId -> navController.navigate(Screen.ListingDetail.createRoute(listingId)) }
                )
            }

            // Category browsing
            composable(
                route = Screen.Category.route,
                arguments = listOf(navArgument("slug") { type = NavType.StringType })
            ) { backStackEntry ->
                val slug = backStackEntry.arguments?.getString("slug") ?: ""
                BrowseScreen(
                    onNavigateToCategory = { categorySlug -> navController.navigate(Screen.Category.createRoute(categorySlug)) },
                    onNavigateToSearch = { navController.navigate(Screen.Search.createRoute("")) }
                )
            }
        }
    }
}
