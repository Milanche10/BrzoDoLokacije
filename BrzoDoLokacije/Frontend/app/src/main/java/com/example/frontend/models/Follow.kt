package com.example.frontend.models

import com.example.frontend.UserInfoItem

data class Follow (
    val from: UserInfoItem?,
    val to : UserInfoItem?,
    val id: Long )