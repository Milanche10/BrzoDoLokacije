package com.example.frontend

import com.example.frontend.models.SocialPost

data class CommentDataItem(

    val id: Int? = null,
    val content: String? = null,
    val appUser: UserInfoItem? = null,
    val socialPost: PostCommentInfo? = null
)