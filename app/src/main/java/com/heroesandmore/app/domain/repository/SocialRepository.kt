package com.heroesandmore.app.domain.repository

import com.heroesandmore.app.domain.model.*
import com.heroesandmore.app.util.Resource

interface SocialRepository {
    // Activity Feed
    suspend fun getActivityFeed(cursor: String? = null): Resource<List<Activity>>

    // Following/Followers
    suspend fun getFollowing(): Resource<List<Follow>>
    suspend fun getFollowers(): Resource<List<Follow>>
    suspend fun isFollowing(userId: Int): Resource<Boolean>
    suspend fun followUser(userId: Int): Resource<Boolean>
    suspend fun unfollowUser(userId: Int): Resource<Boolean>

    // Messages
    suspend fun getConversations(): Resource<List<Conversation>>
    suspend fun getMessages(userId: Int, cursor: String? = null): Resource<List<Message>>
    suspend fun sendMessage(userId: Int, content: String): Resource<Message>

    // Forums
    suspend fun getForumCategories(): Resource<List<ForumCategory>>
    suspend fun getForumCategory(slug: String): Resource<ForumCategory>
    suspend fun getForumThreads(categorySlug: String, page: Int = 1): Resource<List<ForumThread>>
    suspend fun createThread(categorySlug: String, title: String, content: String): Resource<ForumThreadDetail>
    suspend fun getThreadDetail(threadId: Int): Resource<ForumThreadDetail>
    suspend fun createPost(threadId: Int, content: String): Resource<ForumPost>
    suspend fun updatePost(postId: Int, content: String): Resource<ForumPost>
    suspend fun deletePost(postId: Int): Resource<Boolean>
}
