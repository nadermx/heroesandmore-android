package com.heroesandmore.app.domain.model

data class Activity(
    val id: Int,
    val type: ActivityType,
    val actor: String,
    val actorAvatar: String?,
    val message: String,
    val targetId: Int?,
    val targetType: String?,
    val targetTitle: String?,
    val targetImage: String?,
    val created: String
)

enum class ActivityType {
    LISTING_CREATED,
    LISTING_SOLD,
    COLLECTION_UPDATED,
    REVIEW_RECEIVED,
    FOLLOWED,
    FORUM_POST;

    companion object {
        fun fromString(value: String): ActivityType {
            return when (value.lowercase()) {
                "listing_created" -> LISTING_CREATED
                "listing_sold" -> LISTING_SOLD
                "collection_updated" -> COLLECTION_UPDATED
                "review_received" -> REVIEW_RECEIVED
                "followed" -> FOLLOWED
                "forum_post" -> FORUM_POST
                else -> LISTING_CREATED
            }
        }
    }
}

data class Follow(
    val id: Int,
    val userId: Int,
    val username: String,
    val avatar: String?,
    val bio: String?,
    val isSellerVerified: Boolean,
    val followedAt: String
)

data class Conversation(
    val id: Int,
    val otherUserId: Int,
    val otherUsername: String,
    val otherAvatar: String?,
    val lastMessage: String?,
    val lastMessageAt: String?,
    val unreadCount: Int
)

data class Message(
    val id: Int,
    val senderId: Int,
    val senderUsername: String,
    val senderAvatar: String?,
    val content: String,
    val read: Boolean,
    val created: String
)

data class ForumCategory(
    val id: Int,
    val name: String,
    val slug: String,
    val description: String?,
    val icon: String?,
    val threadCount: Int,
    val postCount: Int,
    val lastPost: ForumPostSummary?
)

data class ForumPostSummary(
    val id: Int,
    val threadId: Int,
    val threadTitle: String,
    val author: String,
    val created: String
)

data class ForumThread(
    val id: Int,
    val title: String,
    val author: String,
    val authorAvatar: String?,
    val replyCount: Int,
    val viewCount: Int,
    val pinned: Boolean,
    val locked: Boolean,
    val lastPostAt: String?,
    val lastPostBy: String?,
    val created: String
)

data class ForumThreadDetail(
    val id: Int,
    val title: String,
    val author: String,
    val authorAvatar: String?,
    val authorId: Int,
    val content: String,
    val replyCount: Int,
    val viewCount: Int,
    val pinned: Boolean,
    val locked: Boolean,
    val posts: List<ForumPost>,
    val created: String
)

data class ForumPost(
    val id: Int,
    val threadId: Int,
    val author: String,
    val authorAvatar: String?,
    val authorId: Int,
    val content: String,
    val isEdited: Boolean,
    val created: String,
    val updated: String?
)
