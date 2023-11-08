package com.example.frontend

import com.example.frontend.models.SocialPost

data class Image(
    val id: Int,
    val name: String,
    val url: String,
    val socialPost : SocialPost?= null
)