package com.example.frontend

data class PostInfoItem(
    val date: Any? = null,
    val description: String? = null,
    val id: Long? = null,
    val images: List<Image>? = null,
    val location: Location? = null,
    val name: String? = null,
    val appUser: UserInfoItem? = null,
    val likeReactions: List<LikesDataItem>? = null,
    val comments: List<Any>? = null
)