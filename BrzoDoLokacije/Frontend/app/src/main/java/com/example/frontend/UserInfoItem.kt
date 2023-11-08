package com.example.frontend

import com.example.frontend.models.Follow

data class UserInfoItem(
    val comments: List<Any>? = null,
    val email: String? = null,
    val firstName: String? = null,
    val id: Long? = null,
    val image: Image? = null,
    val lastName: String? = null,
    val likeReactions: List<Any>? = null,
    val password: String? = null,
    val socialPosts: List<PostDataItem>? = null,
    val following: List<Follow>? = null,
    val followers: List<Follow>? = null
)