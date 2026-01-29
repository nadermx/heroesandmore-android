package com.heroesandmore.app.data.repository

import com.heroesandmore.app.data.api.SocialApi
import com.heroesandmore.app.data.dto.social.*
import com.heroesandmore.app.domain.model.*
import com.heroesandmore.app.domain.repository.SocialRepository
import com.heroesandmore.app.util.Resource
import com.heroesandmore.app.util.safeApiCall
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SocialRepositoryImpl @Inject constructor(
    private val socialApi: SocialApi
) : SocialRepository {

    override suspend fun getActivityFeed(cursor: String?): Resource<List<Activity>> {
        val result = safeApiCall { socialApi.getActivityFeed(cursor) }
        return result.map { response -> response.results.map { it.toActivity() } }
    }

    override suspend fun getFollowing(): Resource<List<Follow>> {
        val result = safeApiCall { socialApi.getFollowing() }
        return result.map { follows -> follows.map { it.toFollow() } }
    }

    override suspend fun getFollowers(): Resource<List<Follow>> {
        val result = safeApiCall { socialApi.getFollowers() }
        return result.map { follows -> follows.map { it.toFollow() } }
    }

    override suspend fun isFollowing(userId: Int): Resource<Boolean> {
        val result = safeApiCall { socialApi.isFollowing(userId) }
        return result.map { it.isFollowing }
    }

    override suspend fun followUser(userId: Int): Resource<Boolean> {
        val result = safeApiCall { socialApi.followUser(userId) }
        return when (result) {
            is Resource.Success -> Resource.success(true)
            is Resource.Error -> Resource.error(result.message ?: "Failed to follow user")
            is Resource.Loading -> Resource.loading()
        }
    }

    override suspend fun unfollowUser(userId: Int): Resource<Boolean> {
        val result = safeApiCall { socialApi.unfollowUser(userId) }
        return when (result) {
            is Resource.Success -> Resource.success(true)
            is Resource.Error -> Resource.error(result.message ?: "Failed to unfollow user")
            is Resource.Loading -> Resource.loading()
        }
    }

    override suspend fun getConversations(): Resource<List<Conversation>> {
        val result = safeApiCall { socialApi.getConversations() }
        return result.map { conversations -> conversations.map { it.toConversation() } }
    }

    override suspend fun getMessages(userId: Int, cursor: String?): Resource<List<Message>> {
        val result = safeApiCall { socialApi.getMessages(userId, cursor) }
        return result.map { response -> response.results.map { it.toMessage() } }
    }

    override suspend fun sendMessage(userId: Int, content: String): Resource<Message> {
        val result = safeApiCall { socialApi.sendMessage(userId, SendMessageRequest(content)) }
        return result.map { it.toMessage() }
    }

    override suspend fun getForumCategories(): Resource<List<ForumCategory>> {
        val result = safeApiCall { socialApi.getForumCategories() }
        return result.map { categories -> categories.map { it.toForumCategory() } }
    }

    override suspend fun getForumCategory(slug: String): Resource<ForumCategory> {
        val result = safeApiCall { socialApi.getForumCategory(slug) }
        return result.map { it.toForumCategory() }
    }

    override suspend fun getForumThreads(categorySlug: String, page: Int): Resource<List<ForumThread>> {
        val result = safeApiCall { socialApi.getForumThreads(categorySlug, page) }
        return result.map { response -> response.results.map { it.toForumThread() } }
    }

    override suspend fun createThread(categorySlug: String, title: String, content: String): Resource<ForumThreadDetail> {
        val result = safeApiCall { socialApi.createThread(categorySlug, CreateThreadRequest(title, content)) }
        return result.map { it.toForumThreadDetail() }
    }

    override suspend fun getThreadDetail(threadId: Int): Resource<ForumThreadDetail> {
        val result = safeApiCall { socialApi.getThread(threadId) }
        return result.map { it.toForumThreadDetail() }
    }

    override suspend fun createPost(threadId: Int, content: String): Resource<ForumPost> {
        val result = safeApiCall { socialApi.createPost(threadId, CreatePostRequest(content)) }
        return result.map { it.toForumPost() }
    }

    override suspend fun updatePost(postId: Int, content: String): Resource<ForumPost> {
        val result = safeApiCall { socialApi.updatePost(postId, UpdatePostRequest(content)) }
        return result.map { it.toForumPost() }
    }

    override suspend fun deletePost(postId: Int): Resource<Boolean> {
        val result = safeApiCall { socialApi.deletePost(postId) }
        return when (result) {
            is Resource.Success -> Resource.success(true)
            is Resource.Error -> Resource.error(result.message ?: "Failed to delete post")
            is Resource.Loading -> Resource.loading()
        }
    }

    // Mapping functions
    private fun ActivityDto.toActivity(): Activity = Activity(
        id = id,
        type = ActivityType.fromString(type),
        actor = actor,
        actorAvatar = actorAvatar,
        message = message,
        targetId = targetId,
        targetType = targetType,
        targetTitle = targetTitle,
        targetImage = targetImage,
        created = created
    )

    private fun FollowDto.toFollow(): Follow = Follow(
        id = id,
        userId = userId,
        username = username,
        avatar = avatar,
        bio = bio,
        isSellerVerified = isSellerVerified,
        followedAt = followedAt
    )

    private fun ConversationDto.toConversation(): Conversation = Conversation(
        id = id,
        otherUserId = otherUserId,
        otherUsername = otherUsername,
        otherAvatar = otherAvatar,
        lastMessage = lastMessage,
        lastMessageAt = lastMessageAt,
        unreadCount = unreadCount
    )

    private fun MessageDto.toMessage(): Message = Message(
        id = id,
        senderId = senderId,
        senderUsername = senderUsername,
        senderAvatar = senderAvatar,
        content = content,
        read = read,
        created = created
    )

    private fun ForumCategoryDto.toForumCategory(): ForumCategory = ForumCategory(
        id = id,
        name = name,
        slug = slug,
        description = description,
        icon = icon,
        threadCount = threadCount,
        postCount = postCount,
        lastPost = lastPost?.toForumPostSummary()
    )

    private fun ForumPostSummaryDto.toForumPostSummary(): ForumPostSummary = ForumPostSummary(
        id = id,
        threadId = threadId,
        threadTitle = threadTitle,
        author = author,
        created = created
    )

    private fun ForumThreadDto.toForumThread(): ForumThread = ForumThread(
        id = id,
        title = title,
        author = author,
        authorAvatar = authorAvatar,
        replyCount = replyCount,
        viewCount = viewCount,
        pinned = pinned,
        locked = locked,
        lastPostAt = lastPostAt,
        lastPostBy = lastPostBy,
        created = created
    )

    private fun ForumThreadDetailDto.toForumThreadDetail(): ForumThreadDetail = ForumThreadDetail(
        id = id,
        title = title,
        author = author,
        authorAvatar = authorAvatar,
        authorId = authorId,
        content = content,
        replyCount = replyCount,
        viewCount = viewCount,
        pinned = pinned,
        locked = locked,
        posts = posts?.map { it.toForumPost() } ?: emptyList(),
        created = created
    )

    private fun ForumPostDto.toForumPost(): ForumPost = ForumPost(
        id = id,
        threadId = threadId,
        author = author,
        authorAvatar = authorAvatar,
        authorId = authorId,
        content = content,
        isEdited = isEdited,
        created = created,
        updated = updated
    )
}
