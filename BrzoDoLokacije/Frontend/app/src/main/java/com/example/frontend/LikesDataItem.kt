package com.example.frontend

data class LikesDataItem(
    val appUser: UserInfoItem,
    val id: Long? = null,
    val socialPost: SocialPost
)