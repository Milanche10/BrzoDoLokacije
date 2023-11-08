package com.example.frontend

data class SocialPost(
    val appUser: UserInfoItem? = null,
    val comments: List<Any>? = null,
    val date: String? = null,
    val description: String? = null,
    val id: Long? = null,
    val images: List<Image>? = null,
    val likeReactions: List<LikeReaction>? = null,
    val location: Location? = null,
    val name: String? = null
)