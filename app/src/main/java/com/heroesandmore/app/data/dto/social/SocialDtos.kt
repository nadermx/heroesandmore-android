package com.heroesandmore.app.data.dto.social

import com.google.gson.annotations.SerializedName

data class ActivityDto(
    val id: Int,
    val type: String,
    val actor: String,
    @SerializedName("actor_avatar")
    val actorAvatar: String?,
    val message: String,
    @SerializedName("target_id")
    val targetId: Int?,
    @SerializedName("target_type")
    val targetType: String?,
    @SerializedName("target_title")
    val targetTitle: String?,
    @SerializedName("target_image")
    val targetImage: String?,
    val created: String
)

data class FollowDto(
    val id: Int,
    @SerializedName("user_id")
    val userId: Int,
    val username: String,
    val avatar: String?,
    val bio: String?,
    @SerializedName("is_seller_verified")
    val isSellerVerified: Boolean,
    @SerializedName("followed_at")
    val followedAt: String
)

data class FollowingStatusResponse(
    @SerializedName("is_following")
    val isFollowing: Boolean
)

data class ConversationDto(
    val id: Int,
    @SerializedName("other_user_id")
    val otherUserId: Int,
    @SerializedName("other_username")
    val otherUsername: String,
    @SerializedName("other_avatar")
    val otherAvatar: String?,
    @SerializedName("last_message")
    val lastMessage: String?,
    @SerializedName("last_message_at")
    val lastMessageAt: String?,
    @SerializedName("unread_count")
    val unreadCount: Int
)

data class MessageDto(
    val id: Int,
    @SerializedName("sender_id")
    val senderId: Int,
    @SerializedName("sender_username")
    val senderUsername: String,
    @SerializedName("sender_avatar")
    val senderAvatar: String?,
    val content: String,
    val read: Boolean,
    val created: String
)

data class SendMessageRequest(
    val content: String
)

data class ForumCategoryDto(
    val id: Int,
    val name: String,
    val slug: String,
    val description: String?,
    val icon: String?,
    @SerializedName("thread_count")
    val threadCount: Int,
    @SerializedName("post_count")
    val postCount: Int,
    @SerializedName("last_post")
    val lastPost: ForumPostSummaryDto?
)

data class ForumPostSummaryDto(
    val id: Int,
    @SerializedName("thread_id")
    val threadId: Int,
    @SerializedName("thread_title")
    val threadTitle: String,
    val author: String,
    val created: String
)

data class ForumThreadDto(
    val id: Int,
    val title: String,
    val author: String,
    @SerializedName("author_avatar")
    val authorAvatar: String?,
    @SerializedName("reply_count")
    val replyCount: Int,
    @SerializedName("view_count")
    val viewCount: Int,
    val pinned: Boolean,
    val locked: Boolean,
    @SerializedName("last_post_at")
    val lastPostAt: String?,
    @SerializedName("last_post_by")
    val lastPostBy: String?,
    val created: String
)

data class ForumThreadDetailDto(
    val id: Int,
    val title: String,
    val author: String,
    @SerializedName("author_avatar")
    val authorAvatar: String?,
    @SerializedName("author_id")
    val authorId: Int,
    val content: String,
    @SerializedName("reply_count")
    val replyCount: Int,
    @SerializedName("view_count")
    val viewCount: Int,
    val pinned: Boolean,
    val locked: Boolean,
    val posts: List<ForumPostDto>?,
    val created: String
)

data class ForumPostDto(
    val id: Int,
    @SerializedName("thread_id")
    val threadId: Int,
    val author: String,
    @SerializedName("author_avatar")
    val authorAvatar: String?,
    @SerializedName("author_id")
    val authorId: Int,
    val content: String,
    @SerializedName("is_edited")
    val isEdited: Boolean,
    val created: String,
    val updated: String?
)

data class CreateThreadRequest(
    val title: String,
    val content: String
)

data class CreatePostRequest(
    val content: String
)

data class UpdatePostRequest(
    val content: String
)
