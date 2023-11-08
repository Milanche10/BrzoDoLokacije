package com.example.frontend

data class PostCommentInfo(
    val date: Any? = null,
    val description: String? = null,
    val id: Long? = null,
    val images: List<Image>? = null,
    val location: Location? = null,
    val name: String? = null,
    val appUser: UserInfoItem? = null,
    val likeReactions: List<Any>? = null,
    val comments: List<CommentDataItem>? = null
)