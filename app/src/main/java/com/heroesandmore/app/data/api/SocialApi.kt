package com.heroesandmore.app.data.api

import com.heroesandmore.app.data.dto.common.PaginatedResponse
import com.heroesandmore.app.data.dto.marketplace.StatusResponse
import com.heroesandmore.app.data.dto.social.*
import retrofit2.Response
import retrofit2.http.*

interface SocialApi {

    // Activity Feed
    @GET("social/feed/")
    suspend fun getActivityFeed(
        @Query("cursor") cursor: String? = null
    ): Response<PaginatedResponse<ActivityDto>>

    // Following/Followers
    @GET("social/following/")
    suspend fun getFollowing(): Response<List<FollowDto>>

    @GET("social/followers/")
    suspend fun getFollowers(): Response<List<FollowDto>>

    @GET("social/follow/{userId}/")
    suspend fun isFollowing(@Path("userId") userId: Int): Response<FollowingStatusResponse>

    @POST("social/follow/{userId}/")
    suspend fun followUser(@Path("userId") userId: Int): Response<StatusResponse>

    @DELETE("social/follow/{userId}/")
    suspend fun unfollowUser(@Path("userId") userId: Int): Response<Unit>

    // Messages
    @GET("social/messages/")
    suspend fun getConversations(): Response<List<ConversationDto>>

    @GET("social/messages/{userId}/")
    suspend fun getMessages(
        @Path("userId") userId: Int,
        @Query("cursor") cursor: String? = null
    ): Response<PaginatedResponse<MessageDto>>

    @POST("social/messages/{userId}/")
    suspend fun sendMessage(
        @Path("userId") userId: Int,
        @Body request: SendMessageRequest
    ): Response<MessageDto>

    // Forums
    @GET("social/forums/")
    suspend fun getForumCategories(): Response<List<ForumCategoryDto>>

    @GET("social/forums/{slug}/")
    suspend fun getForumCategory(@Path("slug") slug: String): Response<ForumCategoryDto>

    @GET("social/forums/{slug}/threads/")
    suspend fun getForumThreads(
        @Path("slug") slug: String,
        @Query("page") page: Int = 1
    ): Response<PaginatedResponse<ForumThreadDto>>

    @POST("social/forums/{slug}/threads/")
    suspend fun createThread(
        @Path("slug") slug: String,
        @Body request: CreateThreadRequest
    ): Response<ForumThreadDetailDto>

    @GET("social/threads/{id}/")
    suspend fun getThread(@Path("id") threadId: Int): Response<ForumThreadDetailDto>

    @POST("social/threads/{id}/posts/")
    suspend fun createPost(
        @Path("id") threadId: Int,
        @Body request: CreatePostRequest
    ): Response<ForumPostDto>

    @PATCH("social/posts/{id}/")
    suspend fun updatePost(
        @Path("id") postId: Int,
        @Body request: UpdatePostRequest
    ): Response<ForumPostDto>

    @DELETE("social/posts/{id}/")
    suspend fun deletePost(@Path("id") postId: Int): Response<Unit>
}
