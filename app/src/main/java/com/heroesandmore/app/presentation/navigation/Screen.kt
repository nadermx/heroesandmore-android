package com.heroesandmore.app.presentation.navigation

sealed class Screen(val route: String) {
    // Main tabs
    object Home : Screen("home")
    object Browse : Screen("browse")
    object Sell : Screen("sell")
    object Collections : Screen("collections")
    object Profile : Screen("profile")

    // Auth
    object Login : Screen("login")
    object Register : Screen("register")
    object ForgotPassword : Screen("forgot_password")

    // Listings
    object ListingDetail : Screen("listing/{listingId}") {
        fun createRoute(listingId: Int) = "listing/$listingId"
    }
    object CreateListing : Screen("create_listing")
    object EditListing : Screen("edit_listing/{listingId}") {
        fun createRoute(listingId: Int) = "edit_listing/$listingId"
    }
    object MyListings : Screen("my_listings")

    // Categories
    object Category : Screen("category/{slug}") {
        fun createRoute(slug: String) = "category/$slug"
    }

    // Search
    object Search : Screen("search?q={query}") {
        fun createRoute(query: String = "") = "search?q=$query"
    }

    // Collections
    object CollectionDetail : Screen("collection/{collectionId}") {
        fun createRoute(collectionId: Int) = "collection/$collectionId"
    }
    object CreateCollection : Screen("create_collection")
    object AddToCollection : Screen("add_to_collection/{listingId}") {
        fun createRoute(listingId: Int) = "add_to_collection/$listingId"
    }

    // Price Guide
    object PriceGuide : Screen("price_guide")
    object PriceGuideItem : Screen("price_guide/{slug}") {
        fun createRoute(slug: String) = "price_guide/$slug"
    }

    // Orders
    object Orders : Screen("orders")
    object OrderDetail : Screen("order/{orderId}") {
        fun createRoute(orderId: Int) = "order/$orderId"
    }

    // Offers
    object MyOffers : Screen("my_offers")

    // Checkout
    object Checkout : Screen("checkout/{listingId}") {
        fun createRoute(listingId: Int) = "checkout/$listingId"
    }

    // Messages
    object Messages : Screen("messages")
    object Conversation : Screen("conversation/{userId}") {
        fun createRoute(userId: Int) = "conversation/$userId"
    }

    // Notifications
    object Notifications : Screen("notifications")

    // Saved
    object SavedListings : Screen("saved")
    object Wishlists : Screen("wishlists")
    object WishlistDetail : Screen("wishlist/{wishlistId}") {
        fun createRoute(wishlistId: Int) = "wishlist/$wishlistId"
    }

    // Scanner
    object Scanner : Screen("scanner")
    object ScanResult : Screen("scan_result/{scanId}") {
        fun createRoute(scanId: Int) = "scan_result/$scanId"
    }

    // Seller
    object SellerDashboard : Screen("seller_dashboard")
    object SellerSetup : Screen("seller_setup")
    object Inventory : Screen("inventory")
    object BulkImport : Screen("bulk_import")

    // Public Profile
    object PublicProfile : Screen("user/{username}") {
        fun createRoute(username: String) = "user/$username"
    }

    // Settings
    object Settings : Screen("settings")
    object EditProfile : Screen("edit_profile")

    // Auctions
    object AuctionEvents : Screen("auctions")
    object AuctionEvent : Screen("auction/{slug}") {
        fun createRoute(slug: String) = "auction/$slug"
    }
    object PlatformAuctions : Screen("platform_auctions")
    object PlatformAuctionDetail : Screen("platform_auction/{slug}") {
        fun createRoute(slug: String) = "platform_auction/$slug"
    }
    object SubmitLot : Screen("submit_lot/{slug}") {
        fun createRoute(slug: String) = "submit_lot/$slug"
    }

    // Forums
    object Forums : Screen("forums")
    object ForumCategory : Screen("forum/{slug}") {
        fun createRoute(slug: String) = "forum/$slug"
    }
    object ForumThread : Screen("thread/{threadId}") {
        fun createRoute(threadId: Int) = "thread/$threadId"
    }
}
