package com.example.frontend.models

import com.example.frontend.Location
import com.example.frontend.UserDataItem
import okhttp3.MultipartBody
import org.json.JSONObject

data class SocialPost(
    val id: Int,
    val name: String,
    val description: String,
    val date: String,
    val location: Location?,
    val appUser: UserDataItem?,
    val likeReactions : MutableList<MultipartBody.Part>?,
    val comments : MutableList<MultipartBody.Part>?,
    val images: MutableList<MultipartBody.Part>?
)